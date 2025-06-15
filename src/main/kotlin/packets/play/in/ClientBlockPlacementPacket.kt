package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.MetadataType
import com.aznos.datatypes.Slot
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.BlockPlaceEvent
import com.aznos.events.EventManager
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerBlockChangePacket
import com.aznos.packets.play.out.ServerChangeGameStatePacket
import com.aznos.packets.play.out.ServerEntityMetadataPacket
import com.aznos.packets.play.out.ServerOpenSignEditorPacket
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.aznos.util.BlockUtils
import com.aznos.world.World
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.items.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.collections.set
import kotlin.ranges.contains
import kotlin.time.Duration.Companion.seconds

/**
 * Packet sent by the client when placing a block
 *
 * @property hand The hand used to place the block (0 for main hand, 1 for off-hand)
 * @property location The location of the block being placed
 * @property face The face of the block being placed (0-5)
 * @property cursorPositionX The X position of the crosshair on the block, 0-1
 * @property cursorPositionY The Y position of the crosshair on the block, 0-1
 * @property cursorPositionZ The Z position of the crosshair on the block, 0-1
 * @property insideBlock Whether the players head is inside a block
 */
class ClientBlockPlacementPacket(data: ByteArray) : Packet(data) {
    val hand: Int
    val blockPos: BlockPositionType.BlockPosition
    val face: Int
    val cursorPositionX: Float
    val cursorPositionY: Float
    val cursorPositionZ: Float
    val insideBlock: Boolean

    init {
        val input = getIStream()
        hand = input.readVarInt()

        blockPos = BlockPositionType.BlockPosition(input.readLong())
        face = input.readVarInt()
        cursorPositionX = input.readFloat()
        cursorPositionY = input.readFloat()
        cursorPositionZ = input.readFloat()
        insideBlock = input.readBoolean()
    }

    override fun apply(client: ClientSession) {
        val heldStack = client.player.getHeldItem()
        if(heldStack.isAir || heldStack.count <= 0) return

        val heldItem = client.player.getHeldItemID()
        val event = BlockPlaceEvent(
            client.player,
            hand,
            blockPos.copy(),
            face,
            cursorPositionX,
            cursorPositionY,
            cursorPositionZ,
            insideBlock
        )

        EventManager.fire(event)
        if(event.isCancelled) return

        when(event.face) {
            0 -> event.blockPos.y -= 1
            1 -> event.blockPos.y += 1
            2 -> event.blockPos.z -= 1
            3 -> event.blockPos.z += 1
            4 -> event.blockPos.x -= 1
            5 -> event.blockPos.x += 1
        }

        val block = Block.getBlockFromID(heldItem) ?: Item.getItemFromID(heldItem) ?: Block.AIR
        handlePlacement(client, block, event, blockPos)
    }

    private fun handlePlacement(client: ClientSession, block: Any, event: BlockPlaceEvent, blockPos: BlockPositionType.BlockPosition) {
        if(block is Item && block == Item.AIR) return
        if(block is Block && block == Block.AIR) return

        val world = client.player.world

        try {
            val dir = BlockUtils.getCardinalDirection(client.player.location.yaw)
            val properties = modifyBlockProperties(client, block, dir, event)

            val stateID = BlockUtils.getStateID(block, properties)
            if (stateID == -1) return

            val clickedStateID = world?.modifiedBlocks[blockPos]?.stateID ?: 0
            val clickedItemID = clickedStateID.let { Item.getIDFromState(it) }

            for (bed in BlockTags.BEDS) {
                if (clickedItemID == bed.id) {
                    handleBedClick(client, blockPos)
                    break
                }
            }

            players.forEach { _ ->
                val entry = handlePlayerSpecificBlockPlacement(event, stateID, client, block)
                world?.modifiedBlocks[event.blockPos] = entry
            }

            if(client.player.gameMode == GameMode.SURVIVAL) {
                handleItemDecrease(client)
            }
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    private fun modifyBlockProperties(
        client: ClientSession,
        block: Any,
        cardinalDirection: String,
        event: BlockPlaceEvent
    ): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for(furnace in BlockTags.FURNANCES) {
            if(block == furnace) {
                properties["facing"] = cardinalDirection
                properties["lit"] = "false"

                if(block == Block.CAMPFIRE) {
                    properties["waterlogged"] = "false"
                    properties["signal_fire"] = "false"
                }
            }
        }

        if(block == Block.END_ROD) {
            properties["facing"] = cardinalDirection
        }

        if(block == Block.GRINDSTONE) {
            properties["facing"] = cardinalDirection
            properties["face"] = "floor"
        }

        if(block == BlockTags.BANNERS || block == BlockTags.SKULLS) {
            properties["rotation"] = BlockUtils.getRotationalDirection(client.player.location.yaw).toString()
        }

        if(block == BlockTags.SIGNS) {
            properties["rotation"] = BlockUtils.getRotationalDirection(client.player.location.yaw).toString()
            properties["waterlogged"] = "false"
        }

        modifyStairProperties(block, cardinalDirection).forEach { (key, value) ->
            properties[key] = value
        }

        modifyRedstoneBlockProperties(client, block, cardinalDirection).forEach { (key, value) ->
            properties[key] = value
        }

        modifyAxisAlignedBlocks(client, block).forEach { (key, value) ->
            properties[key] = value
        }

        if(block is Item) {
            modifyBedBlocks(client, block, cardinalDirection, event).forEach { (key, value) ->
                properties[key] = value
            }
        }

        return properties
    }

    private fun modifyStairProperties(block: Any, cardinalDirection: String): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for(stair in BlockTags.STAIRS) {
            if(block == stair) {
                properties["facing"] = cardinalDirection
                properties["half"] = "bottom"
                properties["shape"] = "straight"
                properties["waterlogged"] = "false"
            }
        }

        return properties
    }

    private fun modifyRedstoneBlockProperties(client: ClientSession, block: Any, cardinalDirection: String): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        if(block == Block.DISPENSER || block == Block.DROPPER) {
            properties["facing"] = cardinalDirection
            properties["triggered"] = "false"
        }

        if(block == Block.PISTON) {
            properties["extended"] = "false"
            properties["facing"] = cardinalDirection
        }

        if(block == Block.OBSERVER) {
            properties["facing"] = cardinalDirection
            properties["powered"] = "false"
        }

        if(block == Block.REPEATER) {
            properties["facing"] = cardinalDirection
            properties["delay"] = "1"
            properties["locked"] = "false"
            properties["powered"] = "false"
        }

        if(block == Block.COMPARATOR) {
            properties["facing"] = cardinalDirection
            properties["mode"] = "compare"
            properties["powered"] = "false"
        }

        if(block == Block.BARREL) {
            properties["facing"] = cardinalDirection
            properties["open"] = "false"
        }

        if(block == Block.LECTERN) {
            properties["facing"] = cardinalDirection
            properties["has_book"] = "false"
            properties["powered"] = "false"
        }

        return properties
    }

    private fun modifyAxisAlignedBlocks(client: ClientSession, block: Any): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for (log in BlockTags.LOGS) {
            if (block == log) {
                properties["axis"] = BlockUtils.getAxisDirection(
                    client.player.location.yaw,
                    client.player.location.pitch
                ).name.lowercase()
            }
        }

        if(block == Item.QUARTZ_PILLAR) {
            properties["axis"] = BlockUtils.getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if(block == Item.CHAIN) {
            properties["axis"] = BlockUtils.getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if(block == Item.BONE_BLOCK) {
            properties["axis"] = BlockUtils.getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if(block == Item.BASALT || block == Item.POLISHED_BASALT) {
            properties["axis"] = BlockUtils.getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        return properties
    }

    private fun modifyBedBlocks(
        client: ClientSession,
        block: Item,
        cardinalDirection: String,
        event: BlockPlaceEvent
    ): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()
        val world = client.player.world ?: return properties

        for(bed in BlockTags.BEDS) {
            if(block == bed) {
                properties["facing"] = cardinalDirection
                properties["part"] = "foot"
                properties["occupied"] = "false"

                val headPos = when (cardinalDirection) {
                    "north" -> event.blockPos.copy(z = event.blockPos.z - 1)
                    "south" -> event.blockPos.copy(z = event.blockPos.z + 1)
                    "west" -> event.blockPos.copy(x = event.blockPos.x - 1)
                    "east" -> event.blockPos.copy(x = event.blockPos.x + 1)
                    else -> event.blockPos
                }

                val props: MutableMap<String, String> = mutableMapOf()
                props["facing"] = cardinalDirection
                props["part"] = "head"
                props["occupied"] = "false"

                val stateID = Item.getStateID(block, props)
                world.modifiedBlocks[headPos] = BlockWithMetadata(block.id, stateID)

                for(player in players) {
                    player.sendPacket(ServerBlockChangePacket(headPos, stateID))
                }
            }
        }

        return properties
    }

    private fun handlePlayerSpecificBlockPlacement(
        event: BlockPlaceEvent, stateID: Int, client: ClientSession, block: Any
    ): BlockWithMetadata {
        client.sendPacket(ServerBlockChangePacket(event.blockPos, stateID))

        return when {
            block is Item && block in BlockTags.SIGNS -> {
                client.sendPacket(ServerOpenSignEditorPacket(event.blockPos))
                BlockWithMetadata(block.id, stateID, listOf("", "", "", ""))
            }

            block is Item && block in BlockTags.SPAWN_EGGS -> {
                handleSpawnEgg(client, block, event)
                BlockWithMetadata(block.id, stateID)
            }

            block is Block -> {
                BlockWithMetadata(block.id, stateID)
            }

            else -> BlockWithMetadata(0, stateID)
        }
    }

    private fun handleItemDecrease(client: ClientSession) {
        val slot = client.player.selectedSlot
        val inv = client.player.inventory
        val held = inv.heldStack(slot)

        if(held.count > 1) {
            held.count--
            client.sendPacket(ServerSetSlotPacket(0, slot + 36, held.toSlotData()))
        } else {
            inv.setHeldSlot(slot, null)
            client.sendPacket(ServerSetSlotPacket(0, slot + 36, Slot.SlotData(false)))
        }
    }

    private fun handleSpawnEgg(client: ClientSession, block: Item, event: BlockPlaceEvent) {
        val itemName = block.name.removeSuffix("_SPAWN_EGG")
        val entity = LivingEntities.entries.find { it.name.equals(itemName, true) }
        val world = client.player.world

        if(entity != null) {
            val location = LocationType.Location(
                event.blockPos.x + 0.5,
                event.blockPos.y,
                event.blockPos.z + 0.5
            )

            val newEntity = LivingEntity()
            val entityType = entity.id

            client.sendPacket(
                ServerSpawnLivingEntityPacket(
                    newEntity.entityID,
                    newEntity.uuid,
                    entityType,
                    location,
                    90f,
                    0,
                    0,
                    0
                )
            )

            world?.livingEntities?.add(
                Pair(
                    newEntity, EntityData(
                        newEntity.uuid,
                        location,
                        entityType,
                        20,
                        90f,
                        0,
                        0,
                        0,
                    )
                )
            )
        }
    }

    private fun handleBedClick(client: ClientSession, blockPos: BlockPositionType.BlockPosition) {
        val world = client.player.world!!
        val time = world.timeOfDay

        if(world.weather == 0) {
            if(time !in 12542..23459) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )

                return
            }
        } else {
            if(time !in 12010..23991) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )
                return
            }
        }

        val metadata = listOf(
            MetadataType.MetadataEntry(6.toByte(), 18, 2),
            MetadataType.MetadataEntry(13.toByte(), 10, true to blockPos),
        )

        for (player in players) {
            player.sendPacket(ServerEntityMetadataPacket(client.player.entityID, metadata))
        }

        world.sleepingPlayers += 1
        if(canSleepNow(world)) handleSleeping(world, client)
    }

    private fun handleSleeping(world: World, client: ClientSession) {
        Bullet.scope.launch {
            delay(5.seconds)
            val world = client.player.world!!
            if(!canSleepNow(world)) return@launch

            for(player in players) {
                player.sendPacket(ServerChangeGameStatePacket(1, 0f))
                world.weather = 0
                player.setTimeOfDay(0)
                handleWakeUp(world, client)
            }
        }
    }

    private fun canSleepNow(world: World): Boolean {
        val time = world.timeOfDay
        val totalPlayers = players.size
        val sleepingPlayers = world.sleepingPlayers

        return if (totalPlayers > 0 && sleepingPlayers >= totalPlayers / 2) {
            if(world.weather == 0) {
                time in 12542..23459
            } else {
                time in 12010..23991
            }
        } else {
            false
        }
    }

    private fun handleWakeUp(world: World, client: ClientSession) {
        if(!canSleepNow(world)) return

        val metadata = listOf(
            MetadataType.MetadataEntry(13.toByte(), 10, false to null),
            MetadataType.MetadataEntry(6.toByte(), 18, 0)
        )

        for(plr in players) {
            plr.sendPacket(ServerEntityMetadataPacket(client.player.entityID, metadata))
        }

        world.sleepingPlayers -= 1
    }
}
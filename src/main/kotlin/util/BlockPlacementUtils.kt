package com.aznos.util

import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.LocationType
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.events.BlockPlaceEvent
import com.aznos.packets.play.out.ServerBlockChangePacket
import com.aznos.packets.play.out.ServerOpenSignEditorPacket
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.items.Item
import kotlin.collections.set

object BlockPlacementUtils {
    fun modifyBlockProperties(
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

        modifyRedstoneBlockProperties(block, cardinalDirection).forEach { (key, value) ->
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

    fun modifyStairProperties(block: Any, cardinalDirection: String): MutableMap<String, String> {
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

    fun modifyRedstoneBlockProperties(block: Any, cardinalDirection: String): MutableMap<String, String> {
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

    fun modifyAxisAlignedBlocks(client: ClientSession, block: Any): MutableMap<String, String> {
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

    fun modifyBedBlocks(
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

    fun handlePlayerSpecificBlockPlacement(
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

    fun handleSpawnEgg(client: ClientSession, block: Item, event: BlockPlaceEvent) {
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
}
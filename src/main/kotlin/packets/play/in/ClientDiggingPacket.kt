package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.Bullet.breakingBlocks
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.BlockBreakEvent
import com.aznos.events.EventManager
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerBlockBreakAnimationPacket
import com.aznos.packets.play.out.ServerBlockChangePacket
import com.aznos.packets.play.out.ServerParticlePacket
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.util.BlockUtils
import com.aznos.util.ItemUtils
import com.aznos.world.World
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.BlockStatus
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.Particles
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.math.cos
import kotlin.math.sin

/**
 * Sent to the server when a player starts/stops digging a block
 *
 * @property status The action the player is taking
 * @property blockPos The block position
 * @property face The face being hit
 */
class ClientDiggingPacket(data: ByteArray) : Packet(data) {
    val status: Int
    val blockPos: BlockPositionType.BlockPosition
    val face: Int

    init {
        val input = getIStream()
        status = input.readVarInt()

        blockPos = BlockPositionType.BlockPosition(input.readLong())

        face = input.readByte().toInt()
    }

    override fun apply(client: ClientSession) {
        val event = BlockBreakEvent(
            client.player,
            status,
            blockPos,
            face
        )
        EventManager.fire(event)
        if(event.isCancelled) return
        val world = client.player.world!!

        if(client.player.gameMode == GameMode.CREATIVE && event.status == BlockStatus.STARTED_DIGGING.id) {
            for (otherPlayer in Bullet.players) {
                if(otherPlayer != client.player) {
                    otherPlayer.sendPacket(
                        ServerBlockChangePacket(
                            event.blockPos,
                            0
                        )
                    )

                    val block = world.modifiedBlocks[event.blockPos]?.stateID ?: Block.GRASS_BLOCK.id
                    sendBlockBreakParticles(otherPlayer, block, event.blockPos)
                }
            }

            removeBlock(world, event.blockPos)
        } else if(client.player.gameMode == GameMode.SURVIVAL) {
            when(event.status) {
                BlockStatus.STARTED_DIGGING.id -> {
                    val breakTime = BlockUtils.getBlockBreakTime(
                        client, world.modifiedBlocks[event.blockPos]?.stateID ?: Block.GRASS_BLOCK
                    )
                    startBlockBreak(client, event.blockPos, breakTime.toInt())
                }

                BlockStatus.CANCELLED_DIGGING.id -> {
                    stopBlockBreak(client, event.blockPos)
                }

                BlockStatus.FINISHED_DIGGING.id -> {
                    handleFinishDigging(client, event)
                }
            }
        }

        when(event.status) {
            BlockStatus.DROP_ITEM.id, BlockStatus.DROP_ITEM_STACK.id -> {
                handleBlockDrop(client, event.status)
            }
        }
    }

    private fun sendBlockBreakParticles(player: Player, block: Int, blockPos: BlockPositionType.BlockPosition) {
        player.sendPacket(
            ServerParticlePacket(
                Particles.Block(block),
                false,
                blockPos.add(0.5, 0.5, 0.5),
                0.2f,
                0.22f,
                0.2f,
                0f,
                25
            )
        )
    }

    private fun removeBlock(world: World, blockPos: BlockPositionType.BlockPosition) {
        if(world.modifiedBlocks.containsKey(blockPos)) {
            world.modifiedBlocks.remove(blockPos)
        } else {
            world.modifiedBlocks[blockPos] = BlockWithMetadata(0, 0)
        }
    }

    private fun startBlockBreak(client: ClientSession, blockPos: BlockPositionType.BlockPosition, breakTime: Int) {
        if (breakingBlocks.containsKey(blockPos)) return

        val job = Bullet.scope.launch {
            val stepTime = breakTime.toLong() / 9

            for(stage in 0..9) {
                for(otherPlayer in Bullet.players) {
                    if(otherPlayer != client.player) {
                        otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(client.player.entityID, blockPos, stage))
                    }
                }

                delay(stepTime)
            }

            for(otherPlayer in Bullet.players) {
                if(otherPlayer != client.player) {
                    otherPlayer.sendPacket(ServerBlockChangePacket(blockPos, 0))
                }
            }

            breakingBlocks.remove(blockPos)
        }

        breakingBlocks[blockPos] = job
    }

    private fun stopBlockBreak(client: ClientSession, blockPos: BlockPositionType.BlockPosition) {
        breakingBlocks[blockPos]?.cancel()
        breakingBlocks.remove(blockPos)

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(otherPlayer.entityID, blockPos, -1))
            }
        }
    }

    private fun handleFinishDigging(client: ClientSession, event: BlockBreakEvent) {
        client.player.status.exhaustion += 0.005f
        val vx = ((Math.random() - 0.5) * 0.1 * 8000).toInt().toShort()
        val vy = (0.1 * 8000).toInt().toShort()
        val vz = ((Math.random() - 0.5) * 0.1 * 8000).toInt().toShort()
        val world = client.player.world ?: return

        val blockId = world.modifiedBlocks[event.blockPos]?.blockID ?: Block.GRASS_BLOCK.id
        val stateID = world.modifiedBlocks[event.blockPos]?.stateID ?: Block.GRASS_BLOCK.id
        val blockObj = Block.getBlockFromID(blockId) ?: Block.GRASS_BLOCK
        val heldItem = client.player.getHeldItem().item

        if(BlockUtils.canHarvestBlock(blockObj, heldItem)) {
            ItemUtils.dropItem(world, event.blockPos, blockObj.id, vx, vy, vz)
        }

        stopBlockBreak(client, event.blockPos)
        sendBlockBreakParticles(client.player, stateID, event.blockPos)
        removeBlock(world, event.blockPos)
        decreaseItemDurability(client, client.player.inventory.heldStack(client.player.selectedSlot))
    }

    private fun handleBlockDrop(client: ClientSession, status: Int) {
        val held = client.player.inventory.heldStack(client.player.selectedSlot)
        if (held.isAir) return

        val dropAll = status == 5
        val toDrop = if (dropAll) held else held.copy(count = 1)

        if (dropAll) {
            client.player.inventory.setHeldSlot(client.player.selectedSlot, null)
        } else {
            val newCount = held.count - 1
            client.player.inventory.setHeldSlot(
                client.player.selectedSlot,
                if (newCount > 0) held.copy(count = newCount) else null
            )
        }

        client.sendPacket(
            ServerSetSlotPacket(
                0, client.player.selectedSlot + 36,
                client.player.inventory.heldStack(client.player.selectedSlot).toSlotData()
            )
        )

        val yaw = Math.toRadians(client.player.location.yaw.toDouble())
        val pitch = Math.toRadians(client.player.location.pitch.toDouble())
        val forwardSpeed = 10
        val dx = -sin(yaw) * cos(pitch) * forwardSpeed
        val dy = -sin(pitch) * forwardSpeed + 0.2225
        val dz = cos(yaw) * cos(pitch) * forwardSpeed

        val vx = (dx * 8000).toInt().toShort()
        val vy = (dy * 8000).toInt().toShort()
        val vz = (dz * 8000).toInt().toShort()

        ItemUtils.dropItem(client.player.world!!, client.player.location.toBlockPosition(), toDrop.id, vx, vy, vz)
    }

    fun decreaseItemDurability(client: ClientSession, itemStack: ItemStack) {
        if(itemStack.isAir) return
        val isTool = BlockTags.TOOLS.find { it.id == itemStack.item.id } != null
        val maxDurability = getMaxItemDurability(itemStack)

        if(isTool && maxDurability > 0) {
            val newDurability = itemStack.damage + 1
            if(newDurability >= maxDurability) {
                client.player.inventory.setHeldSlot(client.player.selectedSlot, null)
                client.sendPacket(
                    ServerSetSlotPacket(
                        0, client.player.selectedSlot + 36,
                        client.player.inventory.heldStack(client.player.selectedSlot).toSlotData()
                    )
                )
            } else {
                itemStack.damage = newDurability
                client.sendPacket(
                    ServerSetSlotPacket(
                        0, client.player.selectedSlot + 36,
                        itemStack.toSlotData()
                    )
                )
            }
        }
    }

    private fun getMaxItemDurability(itemStack: ItemStack): Int {
        for(woodTool in BlockTags.WOODEN_TOOLS) {
            if(itemStack.item.id == woodTool.id) return 59
        }

        for(stoneTool in BlockTags.STONE_TOOLS) {
            if(itemStack.item.id == stoneTool.id) return 131
        }

        for(ironTool in BlockTags.IRON_TOOLS) {
            if(itemStack.item.id == ironTool.id) return 250
        }

        for(goldTool in BlockTags.GOLDEN_TOOLS) {
            if(itemStack.item.id == goldTool.id) return 32
        }

        for(diamondTool in BlockTags.DIAMOND_TOOLS) {
            if(itemStack.item.id == diamondTool.id) return 1561
        }

        for(netheriteTool in BlockTags.NETHERITE_TOOLS) {
            if(itemStack.item.id == netheriteTool.id) return 2031
        }

        return 0
    }
}
package com.aznos.packets.play.`in`

import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.Slot
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.GameMode
import com.aznos.events.BlockPlaceEvent
import com.aznos.events.EventManager
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.util.BedUtils
import com.aznos.util.BlockPlacementUtils
import com.aznos.util.BlockUtils
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.items.Item
import kotlin.collections.set

/**
 * Packet sent by the client when placing a block
 *
 * @property hand The hand used to place the block (0 for main hand, 1 for off-hand)
 * @property blockPos The location of the block being placed
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

    private fun handlePlacement(
        client: ClientSession, block: Any, event: BlockPlaceEvent, blockPos: BlockPositionType.BlockPosition
    ) {
        if(block is Item && block == Item.AIR) return
        if(block is Block && block == Block.AIR) return

        val world = client.player.world

        try {
            val dir = BlockUtils.getCardinalDirection(client.player.location.yaw)
            val properties = BlockPlacementUtils.modifyBlockProperties(client, block, dir, event)

            val stateID = BlockUtils.getStateID(block, properties)
            if (stateID == -1) return

            val clickedStateID = world?.modifiedBlocks[blockPos]?.stateID ?: 0
            val clickedItemID = clickedStateID.let { Item.getIDFromState(it) }

            for (bed in BlockTags.BEDS) {
                if (clickedItemID == bed.id) {
                    BedUtils.handleBedClick(client, blockPos)
                    break
                }
            }

            players.forEach { _ ->
                val entry = BlockPlacementUtils.handlePlayerSpecificBlockPlacement(event, stateID, client, block)
                world?.modifiedBlocks[event.blockPos] = entry
            }

            if(client.player.gameMode == GameMode.SURVIVAL) {
                handleItemDecrease(client)
            }
        } catch (e: IllegalArgumentException) {
            //do nothing
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
}
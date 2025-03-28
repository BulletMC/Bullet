package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server to play a block break animation
 *
 * @param entityID The entity ID of the entity breaking the block
 * @param blockPos The location of the block being broken
 * @param stage The stage of the block break animation (0-9)
 */
class ServerBlockBreakAnimationPacket(
    entityID: Int,
    blockPos: BlockPositionType.BlockPosition,
    stage: Int
) : Packet(0x08) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeBlockPos(blockPos)
        wrapper.writeByte(stage)
    }
}
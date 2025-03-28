package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This packet is used for a number of actions and animations performed by blocks,
 * usually for non-persistent actions
 */
class ServerBlockActionPacket(
    blockPos: BlockPositionType.BlockPosition,
    actionID: Byte,
    actionParam: Byte,
    blockTime: Int
) : Packet(0x0A) {
    init {
        wrapper.writeBlockPos(blockPos)
        wrapper.writeByte(actionID.toInt())
        wrapper.writeByte(actionParam.toInt())
        wrapper.writeVarInt(blockTime)
    }
}
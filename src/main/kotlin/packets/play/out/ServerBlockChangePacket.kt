package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Packet sent by the server to update the client about a block change or when a block is placed
 */
class ServerBlockChangePacket(
    blockPos: BlockPositionType.BlockPosition,
    blockID: Int
) : Packet(0x0B) {
    init {
        wrapper.writeBlockPos(blockPos)
        wrapper.writeVarInt(blockID)
    }
}
package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Packet sent by the server to update the client about a block change or when a block is placed
 */
class ServerBlockChangePacket(
    private val location: Position,
    private val blockID: Int
) : Packet(0x0B) {
    init {
        val blockPosition: Long = ((location.x and 0x3FFFFFF).toLong() shl 38) or
                ((location.z and 0x3FFFFFF).toLong() shl 12) or
                (location.y and 0xFFF).toLong()

        wrapper.writeLong(blockPosition)
        wrapper.writeVarInt(blockID)
    }
}
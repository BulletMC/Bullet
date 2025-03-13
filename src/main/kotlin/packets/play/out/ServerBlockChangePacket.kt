package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Packet sent by the server to update the client about a block change or when a block is placed
 */
class ServerBlockChangePacket(
    location: Position,
    blockID: Int
) : Packet(0x0B) {
    init {
        val blockPosition: Long = ((location.x.toLong() and 0x3FFFFFF) shl 38) or
                ((location.z.toLong() and 0x3FFFFFF) shl 12) or
                (location.y.toLong() and 0xFFF)

        wrapper.writeLong(blockPosition)
        wrapper.writeVarInt(blockID)
    }
}
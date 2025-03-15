package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Tells the client to unload a chunk column
 * It is legal to send this packet even if the given chunk is not
 * currently loaded by the client
 */
class ServerUnloadChunkPacket(
    x: Int,
    z: Int
) : Packet(0x1C) {
    init {
        wrapper.writeInt(x)
        wrapper.writeInt(z)
    }
}
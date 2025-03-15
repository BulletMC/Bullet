package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent in response to the server keep alive packet to tell the server we're still here
 */
class ClientKeepAlivePacket(data: ByteArray) : Packet(data) {
    val keepAliveID: Long = getIStream().readLong()
}
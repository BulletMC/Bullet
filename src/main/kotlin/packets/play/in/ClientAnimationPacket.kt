package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent to the server when a player swings their arm
*/
class ClientAnimationPacket(data: ByteArray) : Packet(data) {
    val hand: Int = getIStream().readVarInt()
}
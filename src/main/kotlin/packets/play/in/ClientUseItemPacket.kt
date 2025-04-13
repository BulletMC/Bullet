package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent when pressing the use Item key (default: right click)
 * with an item in the hand
 */
class ClientUseItemPacket(data: ByteArray) : Packet(data) {
    val hand: Int = getIStream().readVarInt()
}
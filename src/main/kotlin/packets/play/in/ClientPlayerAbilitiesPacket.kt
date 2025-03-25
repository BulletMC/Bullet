package com.aznos.packets.play.`in`

import com.aznos.packets.Packet

/**
 * The vanilla client sends this packet when the player starts/stops flying
 * with the flags parameter changed accordingly, with the flag being 0x02 of
 * is flying
 */
class ClientPlayerAbilitiesPacket(data: ByteArray) : Packet(data) {
    val flags: Byte = getIStream().readByte()
}
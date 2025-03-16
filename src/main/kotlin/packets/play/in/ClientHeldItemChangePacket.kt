package com.aznos.packets.play.`in`

import com.aznos.packets.Packet

/**
 * Sent when the player changes the slot selection in the hotbar
 */
class ClientHeldItemChangePacket(data: ByteArray) : Packet(data) {
    val slot: Short = getIStream().readShort()
}
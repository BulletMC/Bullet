package com.aznos.packets.play.out

import com.aznos.packets.Packet

/**
 * Sent to change the players slot selection
 *
 * @param slot The slot to be selected (0-8)
 */
class ServerHeldItemChangePacket(
    slot: Byte
) : Packet(0x3F) {
    init {
        wrapper.writeByte(slot.toInt())
    }
}
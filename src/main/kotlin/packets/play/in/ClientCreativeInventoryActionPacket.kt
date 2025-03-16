package com.aznos.packets.play.`in`

import com.aznos.entity.player.data.Slot
import com.aznos.packets.Packet

class ClientCreativeInventoryActionPacket(data: ByteArray) : Packet(data) {
    val slotIndex: Short
    val slot: Slot

    init {
        val input = getIStream()

        slotIndex = input.readShort()
        slot = Slot.read(input)
    }
}
package com.aznos.packets.play.`in`

import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.readSlot
import com.aznos.packets.Packet

class ClientCreativeInventoryActionPacket(data: ByteArray) : Packet(data) {
    val slotIndex: Short
    val slot: Slot.SlotData

    init {
        val input = getIStream()

        slotIndex = input.readShort()
        slot = input.readSlot()
    }
}
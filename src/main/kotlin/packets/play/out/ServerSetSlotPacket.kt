package com.aznos.packets.play.out

import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.writeSlot
import com.aznos.packets.Packet


class ServerSetSlotPacket(
    windowID: Byte,
    slot: Int,
    slotData: Slot.SlotData
) : Packet(0x15) {
    init {
        wrapper.writeByte(windowID.toInt())
        wrapper.writeShort(slot)
        wrapper.writeSlot(slotData)
    }
}
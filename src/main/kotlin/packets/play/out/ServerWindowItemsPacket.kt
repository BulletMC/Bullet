package com.aznos.packets.play.out

import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.writeSlot
import com.aznos.packets.Packet

/**
 * Sent by the server when items in multiple slots (in a way) are added/removed
 * This includes the main inventory, equipped armor, and crafting slots
 *
 * @param windowID The ID of the window which items are being sent for, 0 for player inventory
 * @param slotData Slot data for the slot
 */
class ServerWindowItemsPacket(
    windowID: Byte,
    slotData: List<Slot.SlotData>
) : Packet(0x13) {
    init {
        wrapper.writeByte(windowID.toInt())

        wrapper.writeShort(slotData.size)
        for(slot in slotData) {
            wrapper.writeSlot(slot)
        }
    }
}
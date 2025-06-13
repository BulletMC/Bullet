package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.readSlot
import com.aznos.packets.Packet

class ClientCreativeInventoryActionPacket(data: ByteArray) : Packet(data) {
    val slotIndex: Short
    val slot: Slot.SlotData

    init {
        Bullet.logger.info("Received ClientCreativeInventoryActionPacket")
        val input = getIStream()

        slotIndex = input.readShort()
        Bullet.logger.info("Slot index: $slotIndex")
        slot = input.readSlot()
        Bullet.logger.info("Slot data: $slot")
    }
}
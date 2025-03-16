package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.writeSlot
import com.aznos.packets.Packet

/**
 * Packet sent to all clients to update an entity's equipment
 *
 * @param entityID The ID of the entity whose equipment is being updated
 * @param equipment A list of slot -> item mappings, 0 = Main Hand, 1 = Off-Hand, 2-5 = Armor
 */
class ServerEntityEquipmentPacket(
    entityID: Int,
    equipment: List<Pair<Int, Slot.SlotData>>
) : Packet(0x47) {
    init {
        wrapper.writeVarInt(entityID)

        for((index, entry) in equipment.withIndex()) {
            val (slot, item) = entry
            val slotByte = if(index == equipment.lastIndex) slot else slot or 0x80
            wrapper.writeByte(slotByte)

            wrapper.writeSlot(item)
        }
    }
}
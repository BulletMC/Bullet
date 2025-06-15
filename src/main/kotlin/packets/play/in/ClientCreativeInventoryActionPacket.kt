package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.Slot
import com.aznos.datatypes.Slot.readSlot
import com.aznos.datatypes.Slot.toItemStack
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerEntityEquipmentPacket
import com.aznos.util.ItemUtils

class ClientCreativeInventoryActionPacket(data: ByteArray) : Packet(data) {
    val slotIndex: Short
    val slot: Slot.SlotData

    init {
        val input = getIStream()

        slotIndex = input.readShort()
        slot = input.readSlot()
    }

    override fun apply(client: ClientSession) {
        val slotIdx = slotIndex.toInt()
        val stack = if(slot.present) slot.toItemStack() else null
        client.player.inventory.set(slotIdx, stack)

        if(slotIdx == client.player.selectedSlot + 36) sendHeldItemUpdate(client)
        if(slotIdx == -1) { //drop item
            if(stack != null && !stack.isAir) {
                val vx = ((Math.random() - 0.5) * 0.2 * 8000).toInt().toShort()
                val vy = (0.1 * 8000).toInt().toShort()
                val vz = ((Math.random() - 0.5) * 0.2 * 8000).toInt().toShort()

                ItemUtils.dropItem(client.player.world!!, client.player.location.toBlockPosition(), stack.id, vx, vy, vz)
            }
        }
    }
}
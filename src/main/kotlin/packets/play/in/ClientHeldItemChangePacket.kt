package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.events.EventManager
import com.aznos.events.PlayerHeldItemChangeEvent
import com.aznos.packets.Packet

/**
 * Sent when the player changes the slot selection in the hotbar
 */
class ClientHeldItemChangePacket(data: ByteArray) : Packet(data) {
    val slot: Short = getIStream().readShort()

    override fun apply(client: ClientSession) {
        val event = PlayerHeldItemChangeEvent(client.player, slot.toInt())
        EventManager.fire(event)
        if(event.isCancelled) return

        client.player.selectedSlot = slot.toInt()
        client.player.sendHeldItemUpdate(client)
    }
}
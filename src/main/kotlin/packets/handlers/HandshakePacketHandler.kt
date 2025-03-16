package com.aznos.packets.handlers

import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.events.EventManager
import com.aznos.events.HandshakeEvent
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketReceiver
import packets.handshake.HandshakePacket

class HandshakePacketHandler(client: ClientSession) : PacketHandler(client) {

    @PacketReceiver
    fun onHandshake(packet: HandshakePacket) {
        client.protocol = packet.protocol ?: -1

        val event = HandshakeEvent(client.state, client.protocol)
        EventManager.fire(event)
        if (event.isCancelled) return

        client.changeNetworkState(if (packet.state == 2) GameState.LOGIN else GameState.STATUS)
    }

}
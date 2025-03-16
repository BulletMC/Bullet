package com.aznos.packets.handlers

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.events.EventManager
import com.aznos.events.StatusRequestEvent
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketReceiver
import com.aznos.packets.data.ServerStatusResponse
import com.aznos.packets.status.`in`.ClientStatusPingPacket
import com.aznos.packets.status.`in`.ClientStatusRequestPacket
import com.aznos.packets.status.out.ServerStatusPongPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import packets.status.out.ServerStatusResponsePacket

class StatusPacketHandler(client: ClientSession) : PacketHandler(client) {

    /**
     * Handles a ping packet by sending a pong response and closing the connection
     */
    @PacketReceiver
    fun onPing(packet: ClientStatusPingPacket) {
        client.sendPacket(ServerStatusPongPacket(packet.payload))
        client.close()
    }

    /**
     * Handles a status request packet by sending a server status response
     */
    @PacketReceiver
    fun onStatusRequest(packet: ClientStatusRequestPacket) {
        val event = StatusRequestEvent(Bullet.max_players, 0, Bullet.motd)
        EventManager.fire(event)
        if (event.isCancelled) return

        val response = ServerStatusResponse(
            ServerStatusResponse.Version(Bullet.VERSION, Bullet.PROTOCOL),
            ServerStatusResponse.Players(event.maxPlayers, event.onlinePlayers),
            Component.text(event.motd).color(NamedTextColor.RED),
            Bullet.favicon,
            false
        )

        client.sendPacket(ServerStatusResponsePacket(response))
    }

}
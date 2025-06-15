package com.aznos.packets.status.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.events.EventManager
import com.aznos.events.StatusRequestEvent
import com.aznos.packets.Packet
import com.aznos.packets.data.ServerStatusResponse
import kotlinx.serialization.json.Json
import packets.status.out.ServerStatusResponsePacket

/**
 * Packet representing a status request from the client
 * The server sends back a server status response packet
 */
class ClientStatusRequestPacket(data: ByteArray) : Packet(data) {
    override fun apply(client: ClientSession) {
        val event = StatusRequestEvent(Bullet.max_players, 0, Bullet.motd)
        EventManager.fire(event)
        if(event.isCancelled) return

        val response = ServerStatusResponse(
            ServerStatusResponse.Version(Bullet.VERSION, Bullet.PROTOCOL),
            ServerStatusResponse.Players(event.maxPlayers, event.onlinePlayers),
            event.motd,
            Bullet.favicon,
            false
        )

        client.sendPacket(ServerStatusResponsePacket(Json.encodeToString(response)))
    }
}
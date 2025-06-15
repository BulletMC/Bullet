package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.events.EventManager
import com.aznos.events.PlayerHeartbeatEvent
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerPlayerInfoPacket

/**
 * This packet is sent in response to the server keep alive packet to tell the server we're still here
 */
class ClientKeepAlivePacket(data: ByteArray) : Packet(data) {
    val keepAliveID: Long = getIStream().readLong()

    override fun apply(client: ClientSession) {
        val event = PlayerHeartbeatEvent(client.player)
        EventManager.fire(event)
        if(event.isCancelled) return

        client.respondedToKeepAlive = true

        val receivedTimestamp = keepAliveID
        val currentTime = System.currentTimeMillis()
        val rtt = (currentTime - receivedTimestamp).toInt()

        client.player.ping = rtt / 2

        for(player in Bullet.players) {
            player.sendPacket(
                ServerPlayerInfoPacket(
                    2,
                    client.player.uuid,
                    ping = client.player.ping
                )
            )
        }
    }
}
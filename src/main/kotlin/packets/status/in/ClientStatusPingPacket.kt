package com.aznos.packets.status.`in`

import com.aznos.ClientSession
import com.aznos.packets.Packet
import com.aznos.packets.status.out.ServerStatusPongPacket

/**
 * Packet representing a ping request from the client
 * The server sends back a server status pong packet
 *
 * @property payload The long timestamp of the ping request
 */
class ClientStatusPingPacket(data: ByteArray) : Packet(data) {
    var payload: Long = 0

    init {
        payload = getIStream().readLong()
    }

    override fun apply(client: ClientSession) {
        client.sendPacket(ServerStatusPongPacket(payload))
        client.close()
    }
}

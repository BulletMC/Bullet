package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.Packet

class StatusHandler : PacketHandler {
    override fun handle(packet: Packet, connection: ClientConnection) {
        when(packet) {
            is Packet.Status.StatusRequest -> {
                logger.info("[*] Status request - sending status response")
                connection.send(Packet.Status.StatusResponse(buildStatusJson()))
            }

            is Packet.Status.PingRequest -> {
                logger.info("[*] Ping request payload=${packet.payload}, ponging")
                connection.send(Packet.Status.PongResponse(packet.payload))
            }

            else -> logger.warn("[!] StatusHandler received unexpected packet: $packet")
        }
    }

    private fun buildStatusJson(): String = """
        {
          "version": { "name": "BulletMC 26.1.2", "protocol": 775 },
          "players": { "max": 67, "online": 0, "sample": [] },
          "description": { "text": "§6BulletMC §7- as fast as a Bullet" },
          "enforcesSecureChat": false
        }
    """.trimIndent()
}
package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.status.PingRequest
import com.maddoxh.bullet.network.packet.impl.`in`.status.StatusRequest
import com.maddoxh.bullet.network.packet.impl.out.status.PongResponse
import com.maddoxh.bullet.network.packet.impl.out.status.StatusResponse

class StatusHandler(private val bullet: Bullet) : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is StatusRequest -> {
                logger.info("[*] Status request - sending status response")
                connection.send(StatusResponse(buildStatusJson()))
            }

            is PingRequest -> {
                logger.info("[*] Ping request payload=${packet.payload}, ponging")
                connection.send(PongResponse(packet.payload))
            }

            else -> logger.warn("[!] StatusHandler received unexpected packet: $packet")
        }
    }

    private fun buildStatusJson(): String = """
        {
          "version": { "name": "${bullet.versionName}", "protocol": ${bullet.protocolVersion} },
          "players": { "max": ${bullet.maxPlayers}, "online": ${bullet.onlinePlayers}, "sample": [] },
          "description": { "text": "${bullet.motd}" },
          "enforcesSecureChat": ${bullet.enforcesSecureChat}
        }
    """.trimIndent()
}

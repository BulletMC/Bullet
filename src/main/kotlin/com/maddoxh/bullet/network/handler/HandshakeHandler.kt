package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.handshake.HandshakeIntention
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.state.ConnectionState

class HandshakeHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is HandshakeIntention -> {
                logger.info(
                    "[*] Handshake: proto=${packet.protocolVersion} " +
                    "addr=${packet.serverAddress}:${packet.serverPort} " +
                    "nextState=${packet.nextState}"
                )

                connection.state = when(packet.nextState) {
                    1 -> ConnectionState.STATUS
                    2 -> ConnectionState.LOGIN
                    else -> throw IllegalStateException("Unexpected state ${packet.nextState}")
                }
            }

            else -> logger.warn("[!] HandshakeHandler received unknown packet: $packet")
        }
    }
}

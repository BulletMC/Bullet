package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginStart

class LoginHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is LoginStart -> {
                logger.info("[*] Login Start: name=${packet.name} uuid=${packet.uuid}")
            }

            else -> logger.warn("[!] LoginHandler received unexpected packet: $packet")
        }
    }
}
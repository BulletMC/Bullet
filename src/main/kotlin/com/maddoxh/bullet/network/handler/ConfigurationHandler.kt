package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket

class ConfigurationHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        logger.info("[*] ConfigurationHandler received: $packet")
    }
}
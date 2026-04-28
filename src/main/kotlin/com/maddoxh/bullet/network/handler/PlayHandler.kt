package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.play.ConfirmTeleportation

class PlayHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is ConfirmTeleportation -> {
                logger.info("[*] Teleport confirmed: id=${packet.teleportID}")
            }

            else -> logger.warn("[!] PlayHandler unhandled: $packet")
        }
    }
}
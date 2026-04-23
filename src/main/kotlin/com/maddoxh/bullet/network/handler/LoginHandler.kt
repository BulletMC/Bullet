package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.entity.player.Player
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginAcknowledged
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginStart
import com.maddoxh.bullet.network.packet.impl.out.login.LoginSuccess
import com.maddoxh.bullet.state.ConnectionState
import java.util.UUID

class LoginHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is LoginStart -> {
                logger.info("[*] Login Start: name=${packet.name} uuid=${packet.uuid}")

                val offlineUUID = UUID.nameUUIDFromBytes(
                    "OfflinePlayer:${packet.name}".toByteArray()
                )

                connection.player = Player(
                    entityID   = ClientConnection.nextEntityID(),
                    uuid       = offlineUUID,
                    username   = packet.name,
                    connection = connection
                )

                connection.player?.disconnect("You've been kicked!")

                connection.send(LoginSuccess(offlineUUID, packet.name))
                logger.info("[*] Sent LoginSuccess for ${packet.name}")
            }

            is LoginAcknowledged -> {
                logger.info("[*] Login Acknowledged by ${connection.player?.username}")
                connection.state = ConnectionState.CONFIGURATION
            }

            else -> logger.warn("[!] LoginHandler received unexpected packet: $packet")
        }
    }
}
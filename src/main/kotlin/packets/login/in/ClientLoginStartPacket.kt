package com.aznos.packets.login.`in`

import com.aznos.Bullet
import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.StringType.readString
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.EventManager
import com.aznos.events.PlayerPreJoinEvent
import com.aznos.packets.Packet
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.util.LoginUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.UUID

/**
 * Packet representing a login start packet from the client
 * The server sends back a login success packet
 *
 * @property username The username of the player joining
*/
class ClientLoginStartPacket(data: ByteArray) : Packet(data) {
    var username: String = getIStream().readString()

    override fun apply(client: ClientSession) {
        val preJoinEvent = PlayerPreJoinEvent()
        EventManager.fire(preJoinEvent)
        if(preJoinEvent.isCancelled) return

        val username = username
        val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:$username").toByteArray())

        checkLoginValidity(client, username)

        val player = initializePlayer(client, username, uuid)
        if(!Bullet.onlineMode) {
            val dupes = players.filter {
                it.username == username || it.uuid == uuid
            }

            players.removeAll(dupes)
            dupes.forEach { old ->
                old.sendPacket(
                    ServerLoginDisconnectPacket(
                        Component.text("You are already logged in from another location", NamedTextColor.RED)
                    )
                )

                old.clientSession.close()
            }

            players.add(player)
            LoginUtils.loginPlayer(client)
        }

        LoginUtils.handleOnlineModeJoin(client)
    }

    private fun checkLoginValidity(client: ClientSession, username: String): Boolean {
        if(client.protocol > Bullet.PROTOCOL) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text()
                        .append(Component.text("Your client is outdated, please downgrade to minecraft version"))
                        .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                        .build()
                )
            )

            client.close()
            return false
        } else if(client.protocol < Bullet.PROTOCOL) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text()
                        .append(Component.text("Your client is outdated, please upgrade to minecraft version"))
                        .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                        .build()
                )
            )

            return false
        }

        if(!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) {
            client.sendPacket(ServerLoginDisconnectPacket(Component.text("Invalid username")))
            return false
        }

        return true
    }

    private fun initializePlayer(client: ClientSession, username: String, uuid: UUID): Player {
        val player = Player(client).apply {
            this.username = username
            this.uuid = uuid
            location = LocationType.Location(8.5, 2.0, 8.5)
            onGround = false
        }

        if (player.gameMode != GameMode.SURVIVAL && player.gameMode != GameMode.ADVENTURE) {
            player.canFly = true
        }

        client.player = player
        return player
    }
}
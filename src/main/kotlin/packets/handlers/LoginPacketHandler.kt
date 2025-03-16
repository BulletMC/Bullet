package com.aznos.packets.handlers

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.Location
import com.aznos.events.EventManager
import com.aznos.events.PlayerPreJoinEvent
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketReceiver
import com.aznos.packets.login.`in`.ClientLoginAckPacket
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.out.ServerLoginSuccessPacket
import java.util.UUID

class LoginPacketHandler(client: ClientSession) : PacketHandler(client) {

    @PacketReceiver
    fun onLoginStart(packet: ClientLoginStartPacket) {
        val preJoinEvent = PlayerPreJoinEvent()
        EventManager.fire(preJoinEvent)
        if (preJoinEvent.isCancelled) return

        if (client.protocol > Bullet.PROTOCOL) {
            client.disconnect("Please downgrade your Minecraft version to " + Bullet.VERSION)
            return
        } else if (client.protocol < Bullet.PROTOCOL) {
            client.disconnect("Your client is outdated, please upgrade to Minecraft version " + Bullet.VERSION)
            return
        }

        client.isClientValid(packet)

        val username = packet.username
        val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:${username}").toByteArray())

        client.player = initializePlayer(username, uuid)
        if (!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) {
            client.disconnect("Invalid username")
            return
        }

        client.sendPacket(ServerLoginSuccessPacket(uuid, packet.username))
    }

    @PacketReceiver
    fun onLoginAck(packet: ClientLoginAckPacket) {
        client.changeNetworkState(GameState.CONFIGURATION)
    }

    private fun initializePlayer(username: String, uuid: UUID): Player {
        val player = Player(client)
        player.username = username
        player.uuid = uuid
        player.location = Location(8.5, 200.0, 8.5, 0f, 0f)
        player.gameMode = GameMode.SPECTATOR
        player.onGround = false

        return player
    }

}
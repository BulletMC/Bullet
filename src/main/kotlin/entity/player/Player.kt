package com.aznos.entity.player

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.entity.Entity
import com.aznos.entity.player.data.ChatPosition
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.Location
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerChangeGameStatePacket
import com.aznos.packets.play.out.ServerChatMessagePacket
import com.aznos.packets.play.out.ServerTimeUpdatePacket
import net.kyori.adventure.text.TextComponent
import java.util.UUID
import kotlin.properties.Delegates

/**
 * Represents a player in the game
 *
 * @property clientSession The client session associated with the player
 */
class Player(
    val clientSession: ClientSession
) : Entity() {
    lateinit var username: String
    lateinit var uuid: UUID
    lateinit var location: Location
    var gameMode: GameMode = GameMode.SURVIVAL
        private set
    var onGround by Delegates.notNull<Boolean>()

    /**
     * Sends a packet to the players client session
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        clientSession.sendPacket(packet)
    }

    /**
     * Disconnects the player from the server
     *
     * @param message The message to be shown on why they were disconnected
     */
    fun disconnect(message: String) {
        clientSession.disconnect(message)
    }

    /**
     * Sends a message to the player
     *
     * @param message The message to be sent to the client
     */
    fun sendMessage(message: TextComponent) {
        sendPacket(ServerChatMessagePacket(message, ChatPosition.CHAT, null))
    }

    fun setTimeOfDay(time: Long) {
        sendPacket(ServerTimeUpdatePacket(Bullet.worldAge, time))
    }

    fun setGameMode(mode: GameMode) {
        gameMode = mode
        sendPacket(ServerChangeGameStatePacket(3, mode.id.toFloat()))
    }
}
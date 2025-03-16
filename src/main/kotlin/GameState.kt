package com.aznos

import com.aznos.packets.PacketHandler
import com.aznos.packets.handlers.ConfigPacketHandler
import com.aznos.packets.handlers.HandshakePacketHandler
import com.aznos.packets.handlers.LoginPacketHandler
import com.aznos.packets.handlers.PlayPacketHandler
import com.aznos.packets.handlers.StatusPacketHandler

/**
 * Represents the current state of the game connection
 */
enum class GameState(val packetHandler: Class<out PacketHandler>) {
    HANDSHAKE(HandshakePacketHandler::class.java),
    STATUS(StatusPacketHandler::class.java),
    LOGIN(LoginPacketHandler::class.java),
    CONFIGURATION(ConfigPacketHandler::class.java),
    PLAY(PlayPacketHandler::class.java)
}
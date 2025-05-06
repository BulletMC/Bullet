package com.aznos.entity

import com.aznos.Bullet
import com.aznos.entity.player.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

/**
 * Represents the console sender in the game for commands
 */
object ConsoleSender : Player(null!!) {
    init {
        username = "CONSOLE"
    }

    override fun sendMessage(message: TextComponent) {
        Bullet.logger.info("[Console]: ${message.content()}")
    }

    @Suppress("EmptyFunctionBlock")
    override fun disconnect(message: Component) {}
}
package com.aznos.entity

import com.aznos.Bullet
import com.aznos.commands.CommandSource
import com.aznos.entity.player.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

/**
 * Represents the console sender in the game for commands
 */
object ConsoleSender : CommandSource {
    override val username = "CONSOLE"

    override fun sendMessage(message: TextComponent) {
        Bullet.logger.info("[Console]: ${message.content()}")
    }
}
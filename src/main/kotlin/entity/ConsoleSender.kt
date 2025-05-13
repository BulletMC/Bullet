package com.aznos.entity

import com.aznos.Bullet
import com.aznos.commands.CommandSource
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer

/**
 * Represents the console sender in the game for commands
 */
object ConsoleSender : CommandSource {
    override val username = "CONSOLE"

    override fun sendMessage(message: TextComponent) {
        val ansi = ANSIComponentSerializer.ansi().serialize(message)
        Bullet.logger.info("[Console]: $ansi")
    }
}
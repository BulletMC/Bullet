package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Application entry point
 */
fun main() {
    EventManager.register(PlayerJoinEvent::class.java) { e ->
        e.player.setTabListHeader(Component.text("Bullet").color(NamedTextColor.GOLD))
        e.player.setTabListFooter(Component.text("Runs as fast as a bullet!").color(NamedTextColor.GOLD))
    }

    Bullet.createServer("0.0.0.0")
}
package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Application entry point
 */
fun main() {
    Bullet.createServer("0.0.0.0")
}
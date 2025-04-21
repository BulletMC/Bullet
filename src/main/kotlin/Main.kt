package com.aznos
import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import com.aznos.packets.play.out.ServerTitlePacket
import com.aznos.packets.play.out.TitleAction
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

/**
 * Application entry point
 */
fun main() {
    EventManager.register(PlayerJoinEvent::class.java) { e ->
        e.player.sendPacket(
            ServerTitlePacket(
                TitleAction.SET_TIME_AND_DISPLAY,
            )
        )

        e.player.sendPacket(
            ServerTitlePacket(
                TitleAction.SET_TITLE, Component.text("Hi!").color(NamedTextColor.GREEN)
            )
        )

        e.player.sendPacket(
            ServerTitlePacket(
                TitleAction.SET_SUBTITLE, Component.text("Welcome to the server!").color(NamedTextColor.GREEN)
            )
        )

        println("sent")
    }

    Bullet.createServer("0.0.0.0")
}
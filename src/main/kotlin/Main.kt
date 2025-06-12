package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerArmSwingEvent
import com.aznos.events.PlayerJoinEvent
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * Application entry point
 */
fun main(args: Array<String>) {
    val parser = ArgParser("bullet")

    val address by parser.option(
        ArgType.String,
        shortName = "a",
        fullName = "address",
        description = "Server address"
    ).default("0.0.0.0")

    val port by parser.option(
        ArgType.Int,
        shortName = "p",
        fullName = "port",
        description = "Server port"
    ).default(25565)

    val onlineMode by parser.option(
        ArgType.Boolean,
        shortName = "o",
        fullName = "online-mode",
        description = "Disable online mode"
    ).default(true)

    val shouldPersist by parser.option(
        ArgType.Boolean,
        shortName = "s",
        fullName = "persist",
        description = "Persist the world to save world/player data"
    ).default(true)

    parser.parse(args)

    EventManager.register(PlayerArmSwingEvent::class.java) { e ->
        e.player.world?.spawnNPC(e.player.location, ItemStack(Item.STONE))
    }

    Bullet.createServer(address, port, onlineMode, shouldPersist)
}

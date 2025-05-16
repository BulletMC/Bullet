package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

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

    parser.parse(args)

    EventManager.register(PlayerJoinEvent::class.java) { e ->
        val player = e.player
        player.sendScoreboard("bullet")
    }

    Bullet.createServer(address, port)
}
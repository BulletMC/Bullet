package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

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

    Bullet.createServer(address, port)
}

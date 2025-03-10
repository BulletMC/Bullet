package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.Location
import com.aznos.packets.play.`in`.movement.ClientPlayerPositionAndRotation
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class TeleportCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("tp")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("target", StringArgumentType.greedyString())
                        .executes {
                            context -> executeTeleport(context.source, StringArgumentType.getString(context, "target"))
                        }
                        .then(
                            RequiredArgumentBuilder.argument<Player, String>(
                                "destination", StringArgumentType.greedyString()
                            ).executes { context ->
                                    executeTeleportToDestination(
                                        context.source,
                                        StringArgumentType.getString(context, "target"),
                                        StringArgumentType.getString(context, "destination")
                                    )
                                }
                        )
                )
        )
    }

    private fun executeTeleport(sender: Player, arg: String): Int {
        val coords = parseCoordinates(arg)
        if(coords != null) {
            sender.location = coords
            sender.sendMessage(Component.text(
                "Teleported to ${coords.x}, ${coords.y}, ${coords.z}")
                .color(NamedTextColor.GRAY)
            )
        } else {
            val targetPlayer = Bullet.players.find { it.username.equals(arg, ignoreCase = true) }
            if(targetPlayer != null) {
                sender.location = targetPlayer.location
                sender.sendMessage(Component.text(
                    "Teleported to ${targetPlayer.username}")
                    .color(NamedTextColor.GRAY)
                )
            } else {
                sender.sendMessage(Component.text("Player not found: $arg").color(NamedTextColor.RED))
                return CommandCodes.ILLEGAL_ARGUMENT.id
            }
        }

        return CommandCodes.SUCCESS.id
    }

    private fun executeTeleportToDestination(sender: Player, targetArg: String, destArg: String): Int {
        val targetPlayer = Bullet.players.find { it.username.equals(targetArg, ignoreCase = true) }
        if(targetPlayer == null) {
            sender.sendMessage(Component.text("Player not found: $targetArg").color(NamedTextColor.RED))
            return CommandCodes.ILLEGAL_ARGUMENT.id
        }

        val coords = parseCoordinates(destArg)
        if(coords != null) {
            targetPlayer.location = coords
            sender.sendMessage(Component.text(
                "Teleported ${targetPlayer.username} to coordinates: ${coords.x}, ${coords.y}, ${coords.z}")
                .color(NamedTextColor.GREEN)
            )
        } else {
            val destinationPlayer = Bullet.players.find { it.username.equals(destArg, ignoreCase = true) }
            if(destinationPlayer != null) {
                targetPlayer.location = destinationPlayer.location
                sender.sendMessage(Component.text(
                    "Teleported ${targetPlayer.username} to ${destinationPlayer.username}")
                    .color(NamedTextColor.GREEN)
                )
            } else {
                sender.sendMessage(Component.text("Destination not found: $destArg").color(NamedTextColor.RED))
                return CommandCodes.ILLEGAL_ARGUMENT.id
            }
        }

        return CommandCodes.SUCCESS.id
    }

    /**
     * Parses the input string into a Location object
     *
     * @param input The input string containing coordinates
     * @return A Location object or null if the input is invalid
     */
    private fun parseCoordinates(input: String): Location? {
        val parts = input.trim().split("\\s+".toRegex())
        return try {
            if (parts.size >= 3) {
                val x = parts[0].toDouble()
                val y = parts[1].toDouble()
                val z = parts[2].toDouble()
                val yaw = if (parts.size >= 4) parts[3].toFloat() else 0f
                val pitch = if (parts.size >= 5) parts[4].toFloat() else 0f
                Location(x, y, z, yaw, pitch)
            } else null
        } catch (e: NumberFormatException) {
            null
        }
    }
}
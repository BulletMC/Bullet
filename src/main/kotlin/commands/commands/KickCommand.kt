package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class KickCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("kick")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.word())
                        .then(
                            RequiredArgumentBuilder.argument<Player, String>(
                                "reason",
                                StringArgumentType.greedyString()
                            )
                                .executes { context ->
                                    val username = StringArgumentType.getString(context, "player")
                                    val reason = StringArgumentType.getString(context, "reason")

                                    val player = Bullet.players.find {
                                        it.username.equals(username, ignoreCase = true)
                                    }

                                    if(player != null) {
                                        player.disconnect(
                                            Component.text("You have been kicked for: $reason", NamedTextColor.RED)
                                        )

                                        CommandCodes.SUCCESS.id
                                    } else {
                                        context.source.sendMessage(
                                            Component.text("Player not found", NamedTextColor.RED)
                                        )

                                        CommandCodes.ILLEGAL_ARGUMENT.id
                                    }
                                }
                        )
                )
        )
    }
}
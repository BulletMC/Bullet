package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.commands.commands.suggestions.PlayerSuggestions
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class MessageCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("message")
            .then(
                RequiredArgumentBuilder.argument<CommandSource, String>("target", StringArgumentType.word())
                    .suggests(PlayerSuggestions.playerNameSuggestions())
                    .then(
                        RequiredArgumentBuilder.argument<CommandSource, String>(
                            "text",
                            StringArgumentType.greedyString()
                        )
                            .executes { context ->
                                val sender = context.source
                                val targetName = StringArgumentType.getString(context, "target")
                                val message = StringArgumentType.getString(context, "text")

                                val targetPlayer = Bullet.players.find {
                                    it.username.equals(targetName, ignoreCase = true)
                                }

                                if(targetPlayer == null) {
                                    sender.sendMessage(
                                        Component.text("Player not found").color(NamedTextColor.RED)
                                    )

                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }

                                if(message.isEmpty()) {
                                    sender.sendMessage(
                                        Component.text("Message cannot be empty").color(NamedTextColor.RED)
                                    )

                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }

                                if(message.length > 255) {
                                    sender.sendMessage(
                                        Component.text("Message is too long").color(NamedTextColor.RED)
                                    )

                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }

                                targetPlayer.sendMessage(
                                    Component.text("[PM from ${sender.username}]: ").color(NamedTextColor.GRAY)
                                        .append(Component.text(message).color(NamedTextColor.WHITE))
                                )

                                sender.sendMessage(
                                    Component.text("[PM to ${targetPlayer.username}]: ").color(NamedTextColor.GRAY)
                                        .append(Component.text(message).color(NamedTextColor.WHITE))
                                )

                                CommandCodes.SUCCESS.id
                            }
                    )
            )
        )

        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("msg")
                .redirect(dispatcher.root.getChild("message"))
        )
    }
}
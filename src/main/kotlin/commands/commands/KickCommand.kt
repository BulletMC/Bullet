package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.commands.CommandManager
import com.aznos.commands.commands.suggestions.PlayerSuggestions
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class KickCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("kick")
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("player", StringArgumentType.word())
                        .suggests(PlayerSuggestions.playerNameSuggestions())
                        .then(
                            RequiredArgumentBuilder.argument<CommandSource, String>(
                                "reason",
                                StringArgumentType.greedyString()
                            )
                                .executes { context ->
                                    val sender = context.source
                                    if(!CommandManager.hasModPermission(sender)) {
                                        return@executes CommandCodes.INVALID_PERMISSIONS.id
                                    }

                                    val username = StringArgumentType.getString(context, "player")
                                    val reason = StringArgumentType.getString(context, "reason")

                                    val player = Bullet.players.find {
                                        it.username.equals(username, ignoreCase = true)
                                    }

                                    if(player != null) {
                                        if(player.username.equals(context.source.username, ignoreCase = true)) {
                                            context.source.sendMessage(
                                                Component.text("You can't kick yourself", NamedTextColor.RED)
                                            )

                                            return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                        }

                                        player.disconnect(
                                            Component.text()
                                                .append(Component.text("You have been kicked!\n\n", NamedTextColor.RED))
                                                .append(Component.text("Reason: ", NamedTextColor.RED))
                                                .append(Component.text(reason, NamedTextColor.GRAY))
                                                .build()
                                        )

                                        context.source.sendMessage(
                                            Component.text()
                                                .append(Component.text("Kicked ", NamedTextColor.GRAY))
                                                .append(Component.text(player.username, NamedTextColor.AQUA))
                                                .append(Component.text(" for ", NamedTextColor.GRAY))
                                                .append(Component.text(reason, NamedTextColor.RED))
                                                .build()
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
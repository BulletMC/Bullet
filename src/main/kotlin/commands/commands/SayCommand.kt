package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SayCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("say")
                .requires { sender ->
                    (sender is Player && (sender.permissionLevel == PermissionLevel.MODERATOR ||
                        sender.permissionLevel == PermissionLevel.ADMINISTRATOR)
                    ) || sender is ConsoleSender
                }.then(
                    RequiredArgumentBuilder.argument<CommandSource, String>(
                        "message",
                        StringArgumentType.greedyString()
                    )
                        .executes{ context ->
                            val message = StringArgumentType.getString(context, "message")
                            if(message.isEmpty()) {
                                context.source.sendMessage(
                                    Component.text("Message cannot be empty").color(NamedTextColor.RED)
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            if(message.length > 255) {
                                context.source.sendMessage(
                                    Component.text("Message is too long").color(NamedTextColor.RED)
                                )

                                return@executes  CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            Bullet.broadcast(message)
                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }
}
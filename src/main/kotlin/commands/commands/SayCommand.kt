package com.aznos.commands.commands

import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component

class SayCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("say")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("message", StringArgumentType.greedyString())
                        .executes{ context ->
                            val message = StringArgumentType.getString(context, "message")
                            context.source.clientSession.sendMessage(
                                Component.text(message)
                            )
                            1
                        }
                )
        )
    }
}
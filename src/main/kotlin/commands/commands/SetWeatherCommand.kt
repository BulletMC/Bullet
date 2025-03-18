package com.aznos.commands.commands

import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import java.util.*

class SetWeatherCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("setweather")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("weather", StringArgumentType.word())
                        .executes{ context ->
                            val message = StringArgumentType.getString(context, "weather")
                            when(message.lowercase(Locale.getDefault())) {
                                "clear" -> context.source.world?.weather = 0
                                "rain" -> context.source.world?.weather = 1
                                "thunder" -> context.source.world?.weather = 2
                                else -> {
                                    context.source.sendMessage(Component.text(
                                        "Invalid weather type",
                                    ))

                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }
                            }

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }
}
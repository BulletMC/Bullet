package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.commands.CommandManager
import com.aznos.entity.player.data.PermissionLevel
import com.aznos.packets.play.out.ServerChangeGameStatePacket
import com.aznos.world.data.Difficulty
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*
import java.util.concurrent.CompletableFuture

class SetWeatherCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("setweather")
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("weather", StringArgumentType.word())
                        .suggests(weatherSuggestions())
                        .executes{ context ->
                            val sender = context.source
                            if(!CommandManager.hasModPermission(sender)) {
                                return@executes CommandCodes.INVALID_PERMISSIONS.id
                            }

                            val message = StringArgumentType.getString(context, "weather")
                            when(message.lowercase(Locale.getDefault())) {
                                "clear" -> {
                                    (context.source as Player).world?.weather = 0

                                    for(player in Bullet.players) {
                                        player.sendPacket(ServerChangeGameStatePacket(1, 0f))
                                    }
                                }

                                "rain" -> {
                                    (context.source as Player).world?.weather = 1

                                    for(player in Bullet.players) {
                                        player.sendPacket(ServerChangeGameStatePacket(2, 0f))
                                    }
                                }

                                else -> {
                                    context.source.sendMessage(Component.text(
                                        "Invalid weather type",
                                        NamedTextColor.RED
                                    ))

                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }
                            }

                            context.source.sendMessage(Component.text()
                                .append(Component.text("Weather set to ", NamedTextColor.YELLOW))
                                .append(Component.text(message, NamedTextColor.GREEN))
                                .build()
                            )

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun weatherSuggestions(): SuggestionProvider<CommandSource> {
        return SuggestionProvider { context, builder ->
            builder.suggest("clear")
            builder.suggest("rain")

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
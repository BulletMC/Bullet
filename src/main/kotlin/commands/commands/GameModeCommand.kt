package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.concurrent.CompletableFuture

class GameModeCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("gamemode").then(
                RequiredArgumentBuilder.argument<Player, String>("mode", StringArgumentType.word())
                    .suggests(gameModeSuggestions()).executes { context ->
                        val input = StringArgumentType.getString(context, "mode")
                        val mode = parseGameMode(input)
                        if(mode == null) {
                            context.source.sendMessage(
                                Component.text("Invalid gamemode: $input", NamedTextColor.RED)
                            )

                            return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                        }

                        context.source.setGameMode(mode)
                        context.source.sendMessage(
                            Component.text("Gamemode set to ${mode.name.lowercase()}", NamedTextColor.GREEN)
                        )

                        return@executes CommandCodes.SUCCESS.id
                    }))
    }

    private fun parseGameMode(input: String): GameMode? {
        val intValue = input.toIntOrNull()
        return if(intValue != null) {
            GameMode.entries.firstOrNull {
                it.id == intValue
            }
        } else {
            GameMode.entries.firstOrNull {
                it.name.equals(input, ignoreCase = true)
            }
        }
    }

    private fun gameModeSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            GameMode.entries.forEach { mode ->
                builder.suggest(mode.name.lowercase())
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
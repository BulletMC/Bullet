package com.aznos.commands.commands

import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.PermissionLevel
import com.aznos.world.data.Difficulty
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.concurrent.CompletableFuture

class DifficultyCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("difficulty")
                .requires { player ->
                    player.permissionLevel == PermissionLevel.MODERATOR ||
                            player.permissionLevel == PermissionLevel.ADMINISTRATOR
                }
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("value", StringArgumentType.greedyString())
                        .suggests(difficultySuggestions())
                        .executes{ context ->
                            val value = StringArgumentType.getString(context, "value")
                            val player = context.source

                            for(difficulty in Difficulty.entries) {
                                if(difficulty.name.equals(value, true)) {
                                    player.world?.difficulty = difficulty
                                    player.sendMessage(
                                        Component.text()
                                            .append(Component.text("Difficulty set to ", NamedTextColor.GRAY))
                                            .append(Component.text(difficulty.name, NamedTextColor.YELLOW))
                                            .build()
                                    )

                                    return@executes CommandCodes.SUCCESS.id
                                }
                            }

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun difficultySuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            Difficulty.entries.forEach { difficulty ->
                builder.suggest(difficulty.name.lowercase())
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
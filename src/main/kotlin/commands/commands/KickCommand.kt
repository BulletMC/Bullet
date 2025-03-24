package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import java.util.concurrent.CompletableFuture

class KickCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("kick")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.greedyString())
                        .suggests(kickSuggestions())
                        .executes{ context ->
                            val targetPlayerUsername = StringArgumentType.getString(context, "player")
                            val targetPlayer = Bullet.players.find { it.username == targetPlayerUsername }

                            Bullet.players.forEach {
                                if (targetPlayer != null) {
                                    if(targetPlayer.username == it.username) {
                                        targetPlayer.disconnect(Component.text()
                                            .append(Component.text("You have been kicked!"))
                                            .build())

                                        return@executes CommandCodes.SUCCESS.id
                                    }
                                }
                            }

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun kickSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            Bullet.players.forEach { player ->
                builder.suggest(player.username)
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
package com.aznos.commands.commands

import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.entity.ConsoleSender
import com.aznos.commands.CommandManager
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
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("difficulty")
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("value", StringArgumentType.greedyString())
                        .suggests(difficultySuggestions())
                        .executes{ context ->
                            if(!CommandManager.hasModPermission(context.source)) {
                                return@executes CommandCodes.INVALID_PERMISSIONS.id
                            }

                            val value = StringArgumentType.getString(context, "value")
                            val player = context.source

                            for(difficulty in Difficulty.entries) {
                                if(difficulty.name.equals(value, true)) {
                                    (player as Player).world?.difficulty = difficulty
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

    private fun difficultySuggestions(): SuggestionProvider<CommandSource> {
        return SuggestionProvider { context, builder ->
            Difficulty.entries.forEach { difficulty ->
                builder.suggest(difficulty.name.lowercase())
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
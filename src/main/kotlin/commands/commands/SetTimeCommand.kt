package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import java.util.*
import java.util.concurrent.CompletableFuture

class SetTimeCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("settime")
                .then(
                    RequiredArgumentBuilder.argument<Player, Long>("time", LongArgumentType.longArg())
                        .suggests(timeSuggestions())
                        .executes { context ->
                            val time = LongArgumentType.getLong(context, "time")

                            if(time < 0 || time > 24000) {
                                context.source.sendMessage(
                                    Component.text("Invalid time, must be between 0 and 24000")
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            context.source.setTimeOfDay(time)
                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun timeSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            TimeOfDay.entries.forEach {
                builder.suggest(it.name.lowercase(Locale.getDefault()))
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
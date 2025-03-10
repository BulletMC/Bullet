package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StopCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("stop")
                .executes { context ->
                    Bullet.close()
                    CommandCodes.SUCCESS.id
                }
                .then(
                    LiteralArgumentBuilder.literal<Player>("broadcast")
                        .executes { context ->
                            scheduleShutdown(60.seconds)
                            context.source.sendMessage(Component.text(
                                "Server will shutdown in 60 seconds",
                                NamedTextColor.RED
                            ))

                            CommandCodes.SUCCESS.id
                        }
                        .then(
                            RequiredArgumentBuilder.argument<Player, Int>("delay", IntegerArgumentType.integer(1))
                                .executes { context ->
                                    val delay = IntegerArgumentType.getInteger(context, "delay")
                                    scheduleShutdown(delay.seconds)
                                    context.source.sendMessage(Component.text(
                                        "Server will shutdown in $delay seconds",
                                        NamedTextColor.RED
                                    ))

                                    CommandCodes.SUCCESS.id
                                }
                        )
                )
        )
    }

    /**
     * Schedules a shutdown of the server after a given delay
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun scheduleShutdown(delay: Duration) {
        GlobalScope.launch {
            val intervals = listOf(60, 30, 10, 5)
            for(interval in intervals) {
                if(delay.inWholeSeconds > interval) {
                    delay((delay.inWholeSeconds - interval).seconds)
                    Bullet.broadcast(Component.text("Server will shutdown in $interval seconds", NamedTextColor.RED))
                }
            }

            delay((delay.inWholeSeconds % 60).seconds)
            Bullet.close()
        }
    }
}
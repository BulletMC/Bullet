package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component

class SetTimeCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("settime")
                .then(
                    RequiredArgumentBuilder.argument<Player, Long>("time", LongArgumentType.longArg())
                        .executes { context ->
                            val time = LongArgumentType.getLong(context, "time")

                            if(time < 0 || time > 24000) {
                                context.source.sendMessage(
                                    Component.text("Invalid time, must be between 0 and 24000")
                                )

                                return@executes 0
                            }

                            Bullet.timeOfDay = time
                            context.source.setTimeOfDay(time)

                            1
                        }
                )
        )
    }
}
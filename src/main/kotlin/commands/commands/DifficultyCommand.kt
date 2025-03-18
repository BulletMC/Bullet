package com.aznos.commands.commands

import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.world.data.Difficulty
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class DifficultyCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("difficulty")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("value", StringArgumentType.greedyString())
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
}
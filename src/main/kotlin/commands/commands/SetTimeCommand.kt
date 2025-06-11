package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.commands.CommandManager
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
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
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("settime")
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, Long>("time", LongArgumentType.longArg())
                        .executes { context ->
                            val sender = context.source
                            if(!CommandManager.hasModPermission(sender)) {
                                return@executes CommandCodes.INVALID_PERMISSIONS.id
                            }

                            val time = LongArgumentType.getLong(context, "time")

                            if(time < 0 || time > 24000) {
                                context.source.sendMessage(
                                    Component.text("Invalid time, must be between 0 and 24000")
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            for(player in Bullet.players) {
                                player.setTimeOfDay(time)
                            }

                            return@executes CommandCodes.SUCCESS.id
                        }
                )
        )
    }
}
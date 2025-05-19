package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandManager
import com.aznos.commands.CommandSource
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*

class UnbanCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("unban")
                .executes { context ->
                    val sender = context.source
                    if(!CommandManager.hasModPermission(sender)) {
                        return@executes CommandCodes.INVALID_PERMISSIONS.id
                    }

                    return@executes CommandCodes.SUCCESS.id
                }.then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("player", StringArgumentType.word())
                        .executes { context ->
                            val username = StringArgumentType.getString(context, "player")
                            val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:$username").toByteArray())

                            if(username.equals(context.source.username, true)) {
                                context.source.sendMessage(
                                    Component.text("You can't unban yourself", NamedTextColor.RED)
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            val ban = Bullet.storage.unbanPlayer(uuid)
                            if(ban == null) {
                                context.source.sendMessage(
                                    Component.text("That player is not banned", NamedTextColor.RED)
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            context.source.sendMessage(
                                Component.text()
                                    .append(Component.text("Unbanned ", NamedTextColor.GRAY))
                                    .append(Component.text(username, NamedTextColor.AQUA))
                                    .append(Component.text(" successfully", NamedTextColor.GRAY))
                                    .build()
                            )

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }
}
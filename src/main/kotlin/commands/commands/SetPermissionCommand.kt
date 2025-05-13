package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.commands.commands.suggestions.PlayerSuggestions
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.*
import java.util.concurrent.CompletableFuture

class SetPermissionCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("setpermission")
                .requires { source ->
                    source is Player &&
                    source.permissionLevel == PermissionLevel.ADMINISTRATOR ||
                    source is ConsoleSender
                }
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("target", StringArgumentType.word())
                        .suggests(PlayerSuggestions.playerNameSuggestions())
                        .then(
                            RequiredArgumentBuilder.argument<CommandSource, String>(
                                "permission",
                                StringArgumentType.word()
                            )
                                .suggests(permissionSuggestions())
                                .executes { context ->
                                    val sender = context.source
                                    val targetName = StringArgumentType.getString(context, "target")
                                    val permissionInput =
                                        StringArgumentType.getString(context, "permission").lowercase()

                                    val targetPlayer = Bullet.players.find {
                                        it.username.equals(targetName, ignoreCase = true)
                                    }

                                    if(targetPlayer == null) {
                                        sender.sendMessage(Component.text("Player not found").color(NamedTextColor.RED))
                                        return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                    }

                                    val permission = when (permissionInput) {
                                        "member" -> PermissionLevel.MEMBER
                                        "mod", "moderator" -> PermissionLevel.MODERATOR
                                        "admin", "administrator" -> PermissionLevel.ADMINISTRATOR
                                        else -> {
                                            sender.sendMessage(Component.text(
                                                "Invalid permission level, use member, moderator, or administrator"
                                            ).color(NamedTextColor.RED))
                                            return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                        }
                                    }

                                    targetPlayer.permissionLevel = permission
                                    sender.sendMessage(Component.text(
                                        "Set ${targetPlayer.username}'s permission to $permission"
                                    ).color(NamedTextColor.GREEN))

                                    targetPlayer.sendMessage(
                                        Component.text(
                                            "Your permission level has been set to $permission by ${sender.username}"
                                        ).color(NamedTextColor.GREEN)
                                    )

                                    CommandCodes.SUCCESS.id
                                }
                        )
                )
        )
    }

    private fun permissionSuggestions(): SuggestionProvider<CommandSource> {
        return SuggestionProvider { _, builder ->
            PermissionLevel.entries.forEach {
                builder.suggest(it.name.lowercase(Locale.getDefault()))
            }

            CompletableFuture.completedFuture(builder.build())
        }
    }
}
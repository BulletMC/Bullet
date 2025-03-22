package com.aznos.commands.commands

import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.*
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

@Synchronized
fun removeMutedUser(username: String): Boolean {
    ensureMutedUsersFileExists()
    val currentMutedUsers = Json.decodeFromString<List<MutedUser>>(mutedUsersFile.readText())
    val updatedMutedUsers = currentMutedUsers.filterNot { it.username.equals(username, ignoreCase = true) }

    return if (currentMutedUsers.size != updatedMutedUsers.size) {
        mutedUsersFile.writeText(Json.encodeToString(updatedMutedUsers))
        true
    } else {
        false
    }
}

class UnmuteCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("unmute")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.greedyString())
                        .suggests(muteSuggestions())
                        .executes { context ->
                            val targetPlayerUsername = StringArgumentType.getString(context, "player")
                            ensureMutedUsersFileExists()
                            if (removeMutedUser(targetPlayerUsername)) {
                                context.source.sendMessage(
                                    Component.text("$targetPlayerUsername has been unmuted successfully.")
                                )
                                return@executes CommandCodes.SUCCESS.id
                            } else {
                                context.source.sendMessage(
                                    Component.text("$targetPlayerUsername is not currently muted.")
                                )
                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }
                        }
                )
        )
    }

    private fun muteSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { _, builder ->
            ensureMutedUsersFileExists()
            val mutedUsers: List<MutedUser> = Json.decodeFromString(mutedUsersFile.readText())
            mutedUsers.forEach { mutedUser ->
                builder.suggest(mutedUser.username)
            }
            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
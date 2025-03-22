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

@Serializable
data class MutedUser(val username: String, val reason: String)

val mutedUsersFile = File("muted_users.json")

fun ensureMutedUsersFileExists() {
    if (!mutedUsersFile.exists()) {
        mutedUsersFile.writeText("[]")
    }
}

@Synchronized
fun addMutedUser(mutedUser: MutedUser): Boolean {
    ensureMutedUsersFileExists()
    val currentMutedUsers = Json.decodeFromString<List<MutedUser>>(mutedUsersFile.readText())
    if (currentMutedUsers.any { it.username.equals(mutedUser.username, ignoreCase = true) }) {
        return false
    }

    val updatedMutedUsers = currentMutedUsers + mutedUser
    mutedUsersFile.writeText(Json.encodeToString(updatedMutedUsers))
    return true
}

class MuteCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("mute")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.greedyString())
                        .suggests(muteSuggestions())
                        .executes { context ->
                            val targetPlayerUsername = StringArgumentType.getString(context, "player")
                            val targetPlayer = Bullet.players.find { it.username == targetPlayerUsername }

                            if (targetPlayer != null) {
                                ensureMutedUsersFileExists()
                                if (addMutedUser(MutedUser(username = targetPlayerUsername, reason = "Muted by admin"))) {
                                    targetPlayer.sendMessage(Component.text("You have been muted by an admin."))
                                    return@executes CommandCodes.SUCCESS.id
                                } else {
                                    context.source.sendMessage(Component.text("$targetPlayerUsername is already muted!"))
                                    return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                }
                            }

                            context.source.sendMessage(Component.text("Player $targetPlayerUsername not found!"))
                            return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                        }
                )
        )
    }

    private fun muteSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            Bullet.players.forEach { player ->
                builder.suggest(player.username)
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
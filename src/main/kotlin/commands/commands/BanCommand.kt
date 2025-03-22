package com.aznos.commands.commands

import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
data class BannedUser(val username: String, val reason: String)

val bannedUsersFile = File("banned_users.json")

fun ensureBannedUsersFileExists() {
    if (!bannedUsersFile.exists()) {
        bannedUsersFile.writeText("[]")
    }
}

@Synchronized
fun addBannedUser(bannedUser: BannedUser): Boolean {
    ensureBannedUsersFileExists()
    val currentBannedUsers = Json.decodeFromString<List<BannedUser>>(bannedUsersFile.readText())
    if (currentBannedUsers.any { it.username.equals(bannedUser.username, ignoreCase = true) }) {
        return false
    }

    val updatedBannedUsers = currentBannedUsers + bannedUser
    bannedUsersFile.writeText(Json.encodeToString(updatedBannedUsers))
    return true
}

class BanCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("ban")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.greedyString())
                        .suggests(banSuggestions())
                        .executes{ context ->
                            val targetPlayerUsername = StringArgumentType.getString(context, "player")
                            val targetPlayer = Bullet.players.find { it.username == targetPlayerUsername }

                            Bullet.players.forEach {
                                if (targetPlayer != null) {
                                    if(targetPlayer.username == it.username) {
                                        targetPlayer.disconnect(Component.text()
                                            .append(Component.text("The ban hammer has spoken!"))
                                            .build())
                                        ensureBannedUsersFileExists()
                                        addBannedUser(BannedUser(username = targetPlayerUsername, reason = "The ban hammer has spoken!"))

                                        return@executes CommandCodes.SUCCESS.id
                                    }
                                }
                            }

                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun banSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            Bullet.players.forEach { player ->
                builder.suggest(player.username)
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
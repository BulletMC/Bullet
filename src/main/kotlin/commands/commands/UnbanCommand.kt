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

class UnbanCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("unban")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.greedyString())
                        .suggests(banSuggestions())
                        .executes { context ->
                            val targetPlayerUsername = StringArgumentType.getString(context, "player")
                            ensureBannedUsersFileExists()
                            removeBannedUser(targetPlayerUsername)?.let {
                                return@executes CommandCodes.SUCCESS.id
                            }

                            context.source.sendMessage(Component.text("$targetPlayerUsername is not banned!"))
                            return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                        }
                )
        )
    }

    private fun banSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { _, builder ->
            ensureBannedUsersFileExists()
            val bannedUsers: List<BannedUser> = Json.decodeFromString(bannedUsersFile.readText())
            bannedUsers.forEach { bannedUser ->
                builder.suggest(bannedUser.username)
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }

    @Synchronized
    private fun removeBannedUser(username: String): Boolean? {
        val currentBannedUsers =
            Json.decodeFromString<List<BannedUser>>(bannedUsersFile.readText())
        val updatedBannedUsers = currentBannedUsers.filterNot { it.username.equals(username, ignoreCase = true) }

        return if (currentBannedUsers.size != updatedBannedUsers.size) {
            bannedUsersFile.writeText(Json.encodeToString(updatedBannedUsers))
            true
        } else {
            null
        }
    }
}
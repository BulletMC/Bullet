package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.commands.suggestions.PlayerSuggestions
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.BanData
import com.aznos.util.DurationFormat.getReadableDuration
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class BanCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("ban")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("player", StringArgumentType.word())
                        .suggests(PlayerSuggestions.playerNameSuggestions())
                        .then(
                            RequiredArgumentBuilder.argument<Player, String>(
                                "reason",
                                StringArgumentType.greedyString()
                            )
                                .executes { context ->
                                    val username = StringArgumentType.getString(context, "player")
                                    val fullReason = StringArgumentType.getString(context, "reason")
                                    val (reason, durationStr) = parseReasonAndDuration(fullReason)

                                    banPlayer(username, reason, durationStr, context)
                                    CommandCodes.SUCCESS.id
                                }
                        )

                        .executes { context ->
                            val username = StringArgumentType.getString(context, "player")
                            banPlayer(username, "No reason specified", null, context)
                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun parseReasonAndDuration(fullReason: String): Pair<String, String?> {
        val tokens = fullReason.trim().split(" ")
        if(tokens.size >= 2) {
            val potentialDuration = tokens.last()
            val potentialLiteral = tokens[tokens.size - 2]

            if(potentialLiteral.equals("time", true) && parseTime(potentialDuration) != null) {
                val reasonTokens = tokens.dropLast(2)
                val reason = reasonTokens.joinToString(" ").ifBlank { "No reason specified" }

                return reason to potentialDuration
            }
        }

        return fullReason to null
    }

    private fun banPlayer(
        username: String,
        reason: String,
        durationStr: String?,
        context: CommandContext<Player>
    ) {
        val player = Bullet.players.find { it.username.equals(username, ignoreCase = true) }
        if(player == null) {
            context.source.sendMessage(
                Component.text("Player not found", NamedTextColor.RED)
            )

            return
        }

        val expirationDuration: Duration? = durationStr?.let { parseTime(it) }
        val readableDuration = getReadableDuration(expirationDuration)

        val banData = BanData(
            player.uuid,
            reason,
            expirationDuration ?: Duration.ZERO,
            System.currentTimeMillis(),
            context.source.uuid)

        Bullet.storage.writeBan(banData)

        sendBanMessage(player, readableDuration, reason)
        sendConfirmMessage(context.source, player, readableDuration, reason)
    }

    private fun sendBanMessage(player: Player, expirationText: String, reason: String) {
        player.disconnect(
            Component.text()
                .append(Component.text("You have been banned ", NamedTextColor.RED))
                .append(Component.text(expirationText, NamedTextColor.RED))
                .append(Component.text("!\n\n", NamedTextColor.RED))
                .append(Component.text("Reason: ", NamedTextColor.RED))
                .append(Component.text(reason, NamedTextColor.GRAY))
                .build()
        )
    }

    private fun sendConfirmMessage(source: Player, player: Player, expirationText: String, reason: String) {
        source.sendMessage(
            Component.text()
                .append(Component.text("Banned ", NamedTextColor.GRAY))
                .append(Component.text(player.username, NamedTextColor.AQUA))
                .append(Component.text(" for ", NamedTextColor.GRAY))
                .append(Component.text(reason, NamedTextColor.RED))
                .append(Component.text(" ", NamedTextColor.GRAY))
                .append(Component.text(expirationText, NamedTextColor.GRAY))
                .build()
        )
    }

    private fun parseTime(input: String): Duration? {
        if(
            input.equals("perm", true) ||
            input.equals("permanent", true) ||
            input.equals("forever", true)
        ) return null

        val regex = Regex("^([0-9]+)([dhms])$")
        val match = regex.matchEntire(input) ?: return null
        val amount = match.groupValues[1].toLong()
        val unit = match.groupValues[2]
        return when(unit) {
            "d" -> amount.days
            "h" -> amount.hours
            "m" -> amount.minutes
            "s" -> amount.seconds
            else -> null
        }
    }
}
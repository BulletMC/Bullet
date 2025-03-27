package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.commands.suggestions.PlayerSuggestions
import com.aznos.entity.player.Player
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.time.Instant
import kotlin.math.exp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

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
                                    val input = StringArgumentType.getString(context, "reason")

                                    val parts = input.split(" ")
                                    val reason = parts.dropLast(1).joinToString(" ").ifBlank {
                                        "No reason specified"
                                    }
                                    val lastPart = parts.lastOrNull()

                                    val timeStr = if(lastPart != null && isTimeInput(lastPart)) lastPart else null
                                    val player = Bullet.players.find {
                                        it.username.equals(username, ignoreCase = true)
                                    }

                                    if(player == null) {
                                        context.source.sendMessage(
                                            Component.text("Player not found", NamedTextColor.RED)
                                        )

                                        return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                                    }

                                    val expirationDuration: Duration? = timeStr?.let {
                                        parseTime(it)
                                    }
                                    val expirationTime = expirationDuration?.let {
                                        Instant.now().plus(it.toJavaDuration())
                                    }

                                    sendBanMessage(player, expirationTime, reason)
                                    sendConfirmMessage(context.source, player, expirationTime, reason)

                                    CommandCodes.SUCCESS.id
                                }
                        )
                )
        )
    }

    private fun sendBanMessage(player: Player, expirationTime: Instant?, reason: String) {
        player.disconnect(
            Component.text()
                .append(Component.text("You have been banned ", NamedTextColor.RED))
                .append(Component.text(
                    if(expirationTime != null) "until $expirationTime" else "permanently",
                    NamedTextColor.RED)
                )
                .append(Component.text("!\n\n", NamedTextColor.RED))
                .append(Component.text("Reason: ", NamedTextColor.RED))
                .append(Component.text(reason, NamedTextColor.GRAY))
                .build()
        )
    }

    private fun sendConfirmMessage(source: Player, player: Player, expirationTime: Instant?, reason: String) {
        source.sendMessage(
            Component.text()
                .append(Component.text("Banned ", NamedTextColor.GRAY))
                .append(Component.text(player.username, NamedTextColor.AQUA))
                .append(Component.text(" for ", NamedTextColor.GRAY))
                .append(Component.text(reason, NamedTextColor.GRAY))
                .append(Component.text(
                    if(expirationTime != null) " until $expirationTime" else " permanently",
                    NamedTextColor.GRAY)
                )
                .build()
        )
    }

    private fun isTimeInput(str: String): Boolean {
        return str.matches(Regex("^[0-9]+[dhms]$")) ||
                str.equals("perm", ignoreCase = true) ||
                str.equals("permanent", ignoreCase = true) ||
                str.equals("forever", ignoreCase = true)
    }

    private fun parseTime(input: String): Duration? {
        if(input.equals("perm", true) || input.equals("permanent", true) || input.equals("forever", true)) {
            return null
        }

        val regex = Regex("^([0-9]+)([dhms])$")
        val match = regex.matchEntire(input) ?: return null

        val amount = match.groupValues[1].toLong()
        val unit = match.groupValues[2]

        return when(unit) {
            "d" -> amount.days
            "h" -> amount.hours
            "m" -> amount.minutes
            "s" -> amount.seconds
            else -> return null
        }
    }
}
package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import java.lang.management.ManagementFactory
import java.text.DecimalFormat

class PerformanceCommand {
    private val decimalFormat = DecimalFormat("#.##")

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("performance")
                .requires { sender ->
                    (sender is Player && sender.permissionLevel == PermissionLevel.ADMINISTRATOR) ||
                    sender is ConsoleSender
                }.executes { context ->
                val player = context.source
                player.sendMessage(getPerformanceStats())

                return@executes CommandCodes.SUCCESS.id
            }
        )
    }

    private fun getPerformanceStats(): TextComponent {
        val runtime = Runtime.getRuntime()
        val threadMXBean = ManagementFactory.getThreadMXBean()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()

        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        val maxMemory = runtime.maxMemory() / (1042 * 1024)
        val availableProcessors = osMXBean.availableProcessors
        val systemLoadAverage = osMXBean.systemLoadAverage
        val threadCount = threadMXBean.threadCount
        val peakThreadCount = threadMXBean.peakThreadCount
        val playerCount = Bullet.players.size
        val rawUptime = System.currentTimeMillis() - Bullet.startupTime
        val uptime = formatUptime(rawUptime)

        return Component.text()
            .append(Component.text("⚡ Server Performance ⚡", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.text("\n━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH))
            .append(Component.text("\n→ Memory Usage: ", NamedTextColor.YELLOW)
                .append(Component.text("$usedMemory MB", NamedTextColor.GREEN))
                .append(Component.text(" / $maxMemory MB", NamedTextColor.GRAY))
            )
            .append(Component.text("\n→ CPU Cores: ", NamedTextColor.YELLOW)
                .append(Component.text("$availableProcessors", NamedTextColor.GREEN))
            )
            .append(Component.text("\n→ System Load: ", NamedTextColor.YELLOW)
                .append(Component.text(decimalFormat.format(systemLoadAverage), NamedTextColor.GREEN))
            )
            .append(Component.text("\n→ Active Threads: ", NamedTextColor.YELLOW)
                .append(Component.text("$threadCount", NamedTextColor.GREEN))
                .append(Component.text(" (Peak: $peakThreadCount)", NamedTextColor.GRAY))
            )
            .append(Component.text("\n→ Server Uptime: ", NamedTextColor.YELLOW)
                .append(Component.text(uptime, NamedTextColor.GREEN))
            )
            .append(Component.text("\n→ Online Players: ", NamedTextColor.YELLOW)
                .append(Component.text("$playerCount", NamedTextColor.GREEN))
            )
            .append(Component.text("\n━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH))
            .build()
    }

    private fun formatUptime(millis: Long): String {
        val totalSeconds = millis / 1000
        val days = totalSeconds / 86400
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return buildString {
            if (days > 0) append("$days days ")
            if (hours > 0) append("$hours hours ")
            if (minutes > 0) append("$minutes minutes ")
            append("$seconds seconds")
        }.trim()
    }
}
package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
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

    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("performance").executes { context ->
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
            .append(Component.text("\n→ Online Players: ", NamedTextColor.YELLOW)
                .append(Component.text("$playerCount", NamedTextColor.GREEN))
            )
            .append(Component.text("\n━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH))
            .build() as TextComponent
    }
}
package com.aznos.commands.commands

import com.aznos.commands.CommandCodes
import com.aznos.entity.player.Player
import com.aznos.commands.CommandManager
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import java.util.concurrent.CompletableFuture
import kotlin.math.ceil
import kotlin.math.min

class HelpCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("help")
                .executes { context ->
                    sendHelpPage(context.source, 1)
                    return@executes CommandCodes.SUCCESS.id
                }
                .then(
                    RequiredArgumentBuilder.argument<Player, Int>(
                        "page", IntegerArgumentType.integer(1)
                    ).executes { context ->
                        val page = IntegerArgumentType.getInteger(context, "page")
                        sendHelpPage(context.source, page)
                        return@executes CommandCodes.SUCCESS.id
                    }
                )
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("command", StringArgumentType.word())
                        .suggests { context, builder ->
                            CommandManager.dispatcher.root.children.forEach { child ->
                                child.name?.let { name ->
                                    builder.suggest(name)
                                }
                            }

                            CompletableFuture.completedFuture(builder.build())
                        }
                        .executes { context ->
                            val commandName = StringArgumentType.getString(context, "command")
                            val commandNode = CommandManager.dispatcher.root.getChild(commandName)

                            if(commandNode != null) {
                                val details = getCommandDetails(commandNode)
                                if(details is TextComponent) {
                                    context.source.sendMessage(details)
                                }
                            } else {
                                context.source.sendMessage(
                                    Component.text("Command not found: $commandName", NamedTextColor.RED)
                                )

                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            return@executes CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun sendHelpPage(player: Player, page: Int) {
        val commands = CommandManager.dispatcher.root.children.mapNotNull { it.name }.sorted()
        val commandsPerPage = 15
        val totalPages = ceil(commands.size / commandsPerPage.toDouble()).toInt()

        if(commands.isEmpty()) {
            player.sendMessage(Component.text("No commands available", NamedTextColor.RED))
            return
        }

        if(page < 1 || page > totalPages) {
            player.sendMessage(Component.text("Invalid page number, there are $totalPages pages.", NamedTextColor.RED))
            return
        }

        val start = (page - 1) * commandsPerPage
        val end = min(start + commandsPerPage, commands.size)

        val builder = Component.text().content("=== Help Page $page/$totalPages ===\n").color(NamedTextColor.GOLD)
        for(i in start until end) {
            builder.append(Component.text("/${commands[i]}\n", NamedTextColor.YELLOW))
        }

        if(page < totalPages) {
            builder.append(Component.text("Use /help ${page + 1} for next page.", NamedTextColor.GRAY))
        }

        player.sendMessage(builder.build())
    }

    private fun getCommandDetails(node: com.mojang.brigadier.tree.CommandNode<Player>): Component {
        val builder = Component.text().content("Command: /").color(NamedTextColor.GOLD)
            .append(Component.text(node.name ?: "unknown", NamedTextColor.YELLOW))
            .append(Component.newline())

        if(node.children.isNotEmpty()) {
            builder.append(Component.text("Arguments:", NamedTextColor.GRAY))
                .append(Component.newline())

            for(child in node.children) {
                when(child) {
                    is ArgumentCommandNode<*, *> -> {
                        builder.append(Component.text(" - <", NamedTextColor.DARK_AQUA))
                            .append(Component.text(child.name, NamedTextColor.AQUA))
                            .append(Component.text("> ", NamedTextColor.DARK_AQUA))
                            .append(
                                Component.text("- " + child.type::class.simpleName, NamedTextColor.GRAY)
                            )
                            .append(Component.newline())
                    }
                    is LiteralCommandNode<*> -> {
                        builder.append(Component.text(" - ", NamedTextColor.DARK_AQUA))
                            .append(Component.text(child.name, NamedTextColor.AQUA))
                            .append(Component.newline())
                    }
                }
            }
        } else {
            builder.append(Component.text("No arguments required.", NamedTextColor.GRAY))
        }

        return builder.build()
    }
}
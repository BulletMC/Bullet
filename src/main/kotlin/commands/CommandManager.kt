package com.aznos.commands

import com.aznos.commands.data.DoubleProperties
import com.aznos.commands.data.IntegerProperties
import com.aznos.commands.data.StringTypes
import com.aznos.entity.player.Player
import com.aznos.commands.data.GraphCommandNode
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import net.kyori.adventure.text.Component

/**
 * Manages the registration and execution of commands.
 *
 * @property dispatcher The command dispatcher
 */
object CommandManager {
    val dispatcher = CommandDispatcher<Player>()

    /**
     * Registers the default BulletMC commands with the command dispatcher
     */
    fun registerCommands() {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("say")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("message", StringArgumentType.greedyString())
                        .executes{ context ->
                            val message = StringArgumentType.getString(context, "message")
                            context.source.clientSession.sendMessage(
                                Component.text(message)
                            )
                            1
                        }
                )
        )
    }

    /**
     * Builds a command graph from the command dispatcher
     * this is called whenever the server is sending what commands are available to the client,
     * so that the client knows what commands are available and what the structure of them is
     *
     * @param dispatcher The command dispatcher
     * @return A pair of the command graph and the index of the root node
     */
    fun buildCommandGraphFromDispatcher(dispatcher: CommandDispatcher<*>): Pair<MutableList<GraphCommandNode>, Int> {
        val visited = mutableSetOf<CommandNode<*>>()
        val ordering = mutableListOf<CommandNode<*>>()

        traverseCommandNodes(dispatcher.root, visited, ordering)

        val indexMap = ordering.withIndex().associate {
            it.value to it.index
        }

        val graphNodes = ordering.map { node ->
            val nodeType = when(node) {
                is RootCommandNode<*> -> GraphCommandNode.FlagTypes.NodeType.ROOT
                is LiteralCommandNode<*> -> GraphCommandNode.FlagTypes.NodeType.LITERAL
                is ArgumentCommandNode<*, *> -> GraphCommandNode.FlagTypes.NodeType.ARGUMENT
                else -> GraphCommandNode.FlagTypes.NodeType.NONE
            }
            val flagsList = mutableSetOf<GraphCommandNode.FlagEntry>()
            flagsList += GraphCommandNode.FlagTypes.NODE_TYPE.build(nodeType)

            if(node.command != null) flagsList += GraphCommandNode.FlagTypes.EXECUTABLE.build()
            if(node.redirect != null) flagsList += GraphCommandNode.FlagTypes.REDIRECT.build()

            val childrenIndices: List<Int> = node.children.mapNotNull { child -> indexMap[child] }
            val redirectIndex = node.redirect?.let { indexMap[it] }

            val name: String? = when (node) {
                is LiteralCommandNode<*> -> node.literal
                is ArgumentCommandNode<*, *> -> node.name
                else -> null
            }

            val(parser, propertiesValue) = if (node is ArgumentCommandNode<*, *>) {
                getParserAndProperties(node)
            } else {
                null to null
            }

            GraphCommandNode(
                flags = GraphCommandNode.Flags(flagsList),
                children = childrenIndices,
                redirect = redirectIndex,
                name = name,
                parser = parser,
                properties = propertiesValue,
                suggestionsType = null
            )
        }.toMutableList()

        val rootIndex = indexMap[dispatcher.root] ?: 0
        return Pair(graphNodes, rootIndex)
    }

    /**
     * Traverses the command nodes in the dispatcher and adds them to the ordering list
     * This also looks for any child nodes and redirects
     *
     * @param node The current node
     * @param visited The set of visited nodes
     * @param ordering The list of ordered nodes
     */
    private fun traverseCommandNodes(
        node: CommandNode<*>,
        visited: MutableSet<CommandNode<*>>,
        ordering: MutableList<CommandNode<*>>
    ) {
        if(visited.contains(node)) return
        visited.add(node)

        for(child in node.children) {
            traverseCommandNodes(child, visited, ordering)
        }

        node.redirect?.let { traverseCommandNodes(it, visited, ordering) }
        ordering.add(node)
    }

    /**
     * Gets the parser and properties for an argument command node so the client knows what property
     * is of what type
     *
     * @param node The argument command node
     * @return A pair of the parser and properties
     */
    private fun getParserAndProperties(node: ArgumentCommandNode<*, *>): Pair<String?, Any?> {
        return when(node.type) {
            is StringArgumentType ->
                "brigadier:string" to StringTypes.GREEDY
            is IntegerArgumentType -> {
                val (min, max) = handleNumberArgumentType(
                    node.type as IntegerArgumentType,
                    "min",
                    "max",
                    -Int.MAX_VALUE,
                    Int.MAX_VALUE
                )

                var propFlags = 0
                if(min != Int.MIN_VALUE) propFlags = propFlags or 0x01
                if(max != Int.MAX_VALUE) propFlags = propFlags or 0x02

                "brigadier:integer" to IntegerProperties(
                    propFlags.toByte(),
                    if(propFlags and 0x01 != 0) min.toInt() else null,
                    if(propFlags and 0x02 != 0) max.toInt() else null
                )
            }
            is DoubleArgumentType -> {
                val (min, max) = handleNumberArgumentType(
                    node.type as DoubleArgumentType,
                    "min",
                    "max",
                    -Double.MAX_VALUE,
                    Double.MAX_VALUE
                )

                var propFlags = 0
                if(min != -Double.MAX_VALUE) propFlags = propFlags or 0x01
                if(max != Double.MAX_VALUE) propFlags = propFlags or 0x02

                "brigadier:double" to DoubleProperties(
                    propFlags.toByte(),
                    if(propFlags and 0x01 != 0) min.toDouble() else null,
                    if(propFlags and 0x02 != 0) max.toDouble() else null
                )
            }
            is BoolArgumentType -> {
                "brigadier:bool" to null
            }
            else ->
                "brigadier:string" to 2
        }
    }

    /**
     * Helper function to handle the number argument types
     *
     * @param type The argument type [IntegerArgumentType] or [DoubleArgumentType]
     * @param minFieldName The name of the min field
     * @param maxFieldName The name of the max field
     * @param minDefault The default min value
     * @param maxDefault The default max value
     *
     * @return A pair of the min and max values
     */
    private fun <T> handleNumberArgumentType(
        type: T,
        minFieldName: String,
        maxFieldName: String,
        minDefault: Number,
        maxDefault: Number
    ): Pair<Number, Number> {
        val min = try {
            val field = type!!::class.java.getDeclaredField(minFieldName)
            field.isAccessible = true
            field.get(type) as Number
        } catch(e: NoSuchFieldException) {
            minDefault
        }

        val max = try {
            val field = type!!::class.java.getDeclaredField(maxFieldName)
            field.isAccessible = true
            field.get(type) as Number
        } catch(e: NoSuchFieldException) {
            maxDefault
        }

        return min to max
    }
}
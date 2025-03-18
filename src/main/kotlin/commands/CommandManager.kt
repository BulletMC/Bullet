package com.aznos.commands

import com.aznos.Bullet
import com.aznos.commands.commands.*
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
import com.mojang.brigadier.tree.ArgumentCommandNode
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode

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
        SayCommand().register(dispatcher)
        SetTimeCommand().register(dispatcher)
        HelpCommand().register(dispatcher)
        GameModeCommand().register(dispatcher)
        TeleportCommand().register(dispatcher)
        StopCommand().register(dispatcher)
        PerformanceCommand().register(dispatcher)
        SetWeatherCommand().register(dispatcher)
        MessageCommand().register(dispatcher)
        DifficultyCommand().register(dispatcher)
    }

    /**
     * Builds a command graph from the command dispatcher
     * this is called whenever the server is sending what commands are available to the client,
     * so that the client knows what commands are available and what the structure of them is
     *
     * @param dispatcher The command dispatcher
     * @return A pair of the command graph and the index of the root node
     */
    fun buildCommandGraphFromDispatcher(dispatcher: CommandDispatcher<*>): Pair<List<GraphCommandNode>, Int> {
        val visited = mutableSetOf<CommandNode<*>>()
        val ordering = mutableListOf<CommandNode<*>>()

        traverseCommandNodes(dispatcher.root, visited, ordering)

        if(ordering.size > 34) {
            Bullet.logger.warn("Too many command nodes detected (${ordering.size}), trimming to 34")
            ordering.retainAll(ordering.take(34))
        }

        val indexMap = ordering.withIndex().associate { it.value to it.index }
        val graphNodes = ordering.map { node ->
            val typeBits = when (node) {
                is RootCommandNode<*> -> 0
                is LiteralCommandNode<*> -> 1
                is ArgumentCommandNode<*, *> -> 2
                else -> 0
            }

            var flagsInt = typeBits
            if(node.command != null) flagsInt = flagsInt or 0x04
            if(node.redirect != null) flagsInt = flagsInt or 0x08

            val flags: Byte = flagsInt.toByte()
            val childrenIndices: List<Int> = node.children.mapNotNull { child -> indexMap[child] }
            val redirectIndex = node.redirect?.let { indexMap[it] }

            val name: String? = when (node) {
                is LiteralCommandNode<*> -> node.literal
                is ArgumentCommandNode<*, *> -> node.name
                else -> null
            }

            val (parser, propertiesValue) = if (node is ArgumentCommandNode<*, *>) {
                getParserAndProperties(node)
            } else {
                null to null
            }

            GraphCommandNode(
                flags = flags,
                children = childrenIndices,
                redirect = redirectIndex,
                name = name,
                parser = parser,
                properties = propertiesValue,
                suggestionsType = null
            )
        }

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
        if(!visited.add(node)) return
        if(ordering.size >= 34) return

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
        return when(val type = node.type) {
            is StringArgumentType -> getStringProperties(type)
            is IntegerArgumentType -> getIntegerProperties(type)
            is DoubleArgumentType -> getDoubleProperties(type)
            is BoolArgumentType -> "brigadier:bool" to null
            else -> "brigadier:string" to StringTypes.GREEDY.id
        }
    }

    /**
     * Gets the string properties for an argument command node
     *
     * @param type The string argument type
     * @return A pair of the parser and properties
     */
    private fun getStringProperties(type: StringArgumentType): Pair<String?, Any?> {
        val wordType = StringArgumentType.word()
        val greedyType = StringArgumentType.greedyString()

        return when(type) {
            wordType -> "brigadier:string" to StringTypes.SINGLE.id
            greedyType -> "brigadier:string" to StringTypes.GREEDY.id
            else -> "brigadier:string" to StringTypes.QUOTABLE.id
        }
    }

    /**
     * Gets the integer properties for an argument command node
     *
     * @param type The integer argument type
     * @return A pair of the parser and properties
     */
    private fun getIntegerProperties(type: IntegerArgumentType): Pair<String?, Any?> {
        val (min, max) = handleNumberArgumentType(
            type,
            "min",
            "max",
            Int.MIN_VALUE,
            Int.MAX_VALUE
        )

        var propFlags = 0
        if(min != Int.MIN_VALUE) propFlags = propFlags or 0x01
        if(max != Int.MAX_VALUE) propFlags = propFlags or 0x02

        return "brigadier:integer" to IntegerProperties(
            propFlags.toByte(),
            if(propFlags and 0x01 != 0) min.toInt() else null,
            if(propFlags and 0x02 != 0) max.toInt() else null
        )
    }

    /**
     * Gets the double properties for an argument command node
     *
     * @param type The double argument type
     * @return A pair of the parser and properties
     */
    private fun getDoubleProperties(type: DoubleArgumentType): Pair<String?, Any?> {
        val (min, max) = handleNumberArgumentType(
            type,
            "min",
            "max",
            -Double.MAX_VALUE,
            Double.MAX_VALUE
        )

        var propFlags = 0
        if(min != -Double.MAX_VALUE) propFlags = propFlags or 0x01
        if(max != Double.MAX_VALUE) propFlags = propFlags or 0x02

        return "brigadier:double" to DoubleProperties(
            propFlags.toByte(),
            if(propFlags and 0x01 != 0) min.toDouble() else null,
            if(propFlags and 0x02 != 0) max.toDouble() else null
        )
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
package com.aznos.commands.data

import com.aznos.packets.newPacket.ResourceLocation
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or


/**
 * Represents one command node in the command graph
 *
 * @param flags The flags of the command node, the available flags are
 * 0x03 - Node type (0: root, 1: literal, 2: argument)
 * 0x04 - Is executable - Set if the node stack to this point is a valid command
 * 0x08 - Has redirect - Set if the node redirects to another node
 * 0x10 - Has suggestions type - Only present for argument nodes
 *
 * @param children The children of the command node
 * @param redirect The redirect of the command node, only if flags has 0x08
 * @param name The name of the command node, only for argument and literal nodes
 * @param parser An optional parser for argument nodes so that client knows how to parse the argument
 * @param properties An optional property value for argument nodes
 * @param suggestionsType An optional suggestions type if flags contains 0x10
 */
data class GraphCommandNode(
    val flags: Flags,
    val children: List<Int>,
    val redirect: Int? = null,
    val name: String? = null,
    val parser: String? = null,
    val properties: Any? = null,
    val suggestionsType: Optional<ResourceLocation> = Optional.empty()
) {

    data class Flags(val flags: MutableSet<FlagEntry>) {

        companion object {
            fun fromBytes(byte: Byte): Flags {
                val flagEntries = mutableSetOf<FlagEntry>()

                for (flagType in FlagTypes.entries) {
                    val value = (byte and flagType.mask)
                    if (value != 0.toByte()) {
                        flagEntries.add(FlagEntry(flagType, value))
                    }
                }
                return Flags(flagEntries)
            }
        }

        fun buildFlagsByte(): Byte {
            var currentByte: Byte = 0x00

            for (flag in flags) {
                currentByte = (currentByte or flag.value)
            }
            return currentByte
        }
    }

    abstract class FlagType {
        abstract val mask: Byte
    }

    data class FlagEntry(val type: FlagType, val value: Byte)

    object FlagTypes {

        val NODE_TYPE = NodeTypeFlag
        val EXECUTABLE = ExecutableFlag
        val REDIRECT = RedirectFlag
        val SUGGESTIONS_TYPE = SuggestionsTypeFlag

        val entries = listOf(NODE_TYPE, EXECUTABLE, REDIRECT, SUGGESTIONS_TYPE)

        object NodeTypeFlag : FlagType() {
            override val mask: Byte = 0x03

            fun build(nodeType: NodeType): FlagEntry {
                return FlagEntry(this, (nodeType.ordinal.toByte() and mask))
            }
        }

        object ExecutableFlag : FlagType() {
            override val mask: Byte = 0x04

            fun build(): FlagEntry {
                return FlagEntry(this, 0x04)
            }
        }

        object RedirectFlag : FlagType() {
            override val mask: Byte = 0x08
            fun build(): FlagEntry {
                return FlagEntry(this, 0x08)
            }
        }
        object SuggestionsTypeFlag : FlagType() {
            override val mask: Byte = 0x10
            fun build(): FlagEntry {
                return FlagEntry(this, 0x10)
            }
        }

        enum class NodeType {
            ROOT,
            LITERAL,
            ARGUMENT,
            NONE;

            companion object {
                fun fromByte(byte: Byte): NodeType {
                    // Mask the byte to get only the bits corresponding to NodeType
                    val value = byte and 0x03

                    // Return the corresponding NodeType, or NONE if out of range
                    return NodeType.entries.getOrNull(value.toInt()) ?: NONE
                }
            }

        }
    }
}
package com.aznos.packets.play.out

import com.aznos.commands.data.DoubleProperties
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.commands.data.GraphCommandNode
import com.aznos.commands.data.IntegerProperties
import java.io.IOException

/**
 * This packet is sent to the client when the client joins the game
 * and is used to declare the commands that the client can use
 *
 * See [GraphCommandNode] for more information on what the flags mean
 *
 * @param nodes The nodes of the command graph
 * @param rootIndex The index of the root node in the command graph
 */
class ServerDeclareCommandsPacket(
    nodes: List<GraphCommandNode>,
    rootIndex: Int
) : Packet(0x10) {
    init {
        wrapper.writeVarInt(nodes.size)
        for(node in nodes) {
            wrapper.writeByte(node.flags.toInt())
            wrapper.writeVarInt(node.children.size)

            for(child in node.children) {
                wrapper.writeVarInt(child)
            }

            if(node.flags.toInt() and 0x08 != 0) {
                if(node.redirect == null) {
                    throw IOException("Redirect flag set but no redirect node index provided")
                }
                wrapper.writeVarInt(node.redirect)
            }

            if(node.name != null) {
                wrapper.writeString(node.name)
            }

            if(node.parser != null) {
                wrapper.writeString(node.parser)
                when(val props = node.properties) {
                    is Int -> {
                        wrapper.writeVarInt(props)
                    }
                    is IntegerProperties -> {
                        wrapper.writeByte(props.flags.toInt())
                        if(props.flags.toInt() and 0x01 != 0) {
                            wrapper.writeVarInt(props.min!!)
                        }

                        if(props.flags.toInt() and 0x02 != 0) {
                            wrapper.writeVarInt(props.max!!)
                        }
                    }
                    is DoubleProperties -> {
                        wrapper.writeByte(props.flags.toInt())
                        if(props.flags.toInt() and 0x01 != 0) {
                            wrapper.writeDouble(props.min!!)
                        }

                        if(props.flags.toInt() and 0x02 != 0) {
                            wrapper.writeDouble(props.max!!)
                        }
                    }
                }
            }

            if(node.flags.toInt() and 0x10 != 0) {
                if(node.suggestionsType == null) {
                    throw IOException("Suggestions flag set but no suggestions type provided")
                }

                wrapper.writeString(node.suggestionsType)
            }
        }

        wrapper.writeVarInt(rootIndex)
    }
}
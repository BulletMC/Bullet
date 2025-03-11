package com.aznos.packets.play.out

import com.aznos.commands.data.GraphCommandNode
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.newPacket.Keyed
import com.aznos.packets.newPacket.ResourceLocation
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
    var nodes: MutableList<GraphCommandNode>,
    var rootIndex: Int
) : com.aznos.packets.newPacket.ServerPacket(key) {

    companion object {
        val key = Keyed(0x11, ResourceLocation.vanilla("play.out.commands"))
    }

    override fun retrieveData(): ByteArray {
        return writeData {
            writeVarInt(nodes.size)
            for(node in nodes) {
                val nodeFlagsInt = node.flags.buildFlagsByte().toInt()

                writeByte(nodeFlagsInt)
                writeVarInt(node.children.size)

                for(child in node.children) {
                    writeVarInt(child)
                }

                if(node.flags.flags.any { it.type is GraphCommandNode.FlagTypes.RedirectFlag }) {
                    if(node.redirect == null) {
                        throw IOException("Redirect flag set but no redirect node index provided")
                    }
                    writeVarInt(node.redirect)
                }

                if(node.name != null) {
                    writeString(node.name)
                }

                if(node.parser != null) {
                    writeString(node.parser)
                    if(node.properties is Int) {
                        writeVarInt(node.properties)
                    }
                }

                if(node.flags.flags.any { it.type is GraphCommandNode.FlagTypes.SuggestionsTypeFlag }) {
                    if(node.suggestionsType == null) {
                        throw IOException("Suggestions flag set but no suggestions type provided")
                    }

                    writeString(node.suggestionsType)
                }
            }

            writeVarInt(rootIndex)
        }
    }
}
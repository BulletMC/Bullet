package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.commands.CommandManager
import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerTabCompletePacket

class ClientTabCompletePacket(data: ByteArray) : Packet(data) {
    val transactionID: Int
    val text: String

    init {
        val input = getIStream()

        transactionID = input.readVarInt()
        text = input.readString()
    }

    override fun apply(client: ClientSession) {
        val dispatcher = CommandManager.dispatcher
        val rawInput = text
        val input = if(rawInput.startsWith("/")) rawInput.substring(1) else rawInput

        val parseResults = dispatcher.parse(input, client.player)
        dispatcher.getCompletionSuggestions(parseResults, input.length).thenAccept { suggestions ->
            val lastSpace = input.lastIndexOf(' ')
            val start = lastSpace + 1
            val length = input.length - start

            val startStr = input.substring(start)
            val matches = suggestions.list
                .filter { it.text.startsWith(startStr, ignoreCase = true) }
                .map { it.text }

            val formattedMatches = matches.map { match ->
                if (lastSpace == -1) "/$match" else match
            }

            client.player.sendPacket(
                ServerTabCompletePacket(
                    transactionID,
                    start = start + 1,
                    length = length,
                    matches = formattedMatches
                )
            )
        }
    }
}
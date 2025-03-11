package com.aznos.packets.play.out

import com.aznos.Bullet
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Packet sent by the server to the client in response to a tab completion request
 *
 * @param transactionID The transaction ID of the request
 * @param start Start of the text to replace
 * @param matches List of possible completions
 */
class ServerTabCompletePacket(
    transactionID: Int,
    start: Int,
    matches: List<String>
) : Packet(0x0F) {
    init {
        wrapper.writeVarInt(transactionID)
        wrapper.writeVarInt(start)

        wrapper.writeVarInt(matches.size)
        for(match in matches) {
            Bullet.logger.info("Match: $match")
            wrapper.writeUTF(match)
            wrapper.writeBoolean(false)
        }
    }
}
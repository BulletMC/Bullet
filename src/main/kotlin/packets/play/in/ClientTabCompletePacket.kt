package com.aznos.packets.play.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent by the client to request a list of possible completions for the given text
 *
 * @property transactionID The ID received in the tab completion request packet
 * @property text All text behind the cursor without the /
 */
class ClientTabCompletePacket(data: ByteArray) : Packet(data) {
    val transactionID: Int
    val text: String

    init {
        val input = getIStream()

        transactionID = input.readVarInt()
        text = input.readString()
    }
}
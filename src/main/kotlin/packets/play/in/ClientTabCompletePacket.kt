package com.aznos.packets.play.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

class ClientTabCompletePacket(data: ByteArray) : Packet(data) {
    val transactionID: Int
    val text: String

    init {
        val input = getIStream()

        transactionID = input.readVarInt()
        text = input.readString()
    }
}
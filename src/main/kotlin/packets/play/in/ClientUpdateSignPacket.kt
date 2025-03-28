package com.aznos.packets.play.`in`

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.readBlockPos
import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This message is sent from the client to the server when the "Done" button is
 * pushed after placing a sign and editing the text.
 */
class ClientUpdateSignPacket(data: ByteArray) : Packet(data) {
    val blockPos: BlockPositionType.BlockPosition
    val line1: String
    val line2: String
    val line3: String
    val line4: String

    init {
        val input = getIStream()

        blockPos = input.readBlockPos()
        line1 = input.readString()
        line2 = input.readString()
        line3 = input.readString()
        line4 = input.readString()
    }
}
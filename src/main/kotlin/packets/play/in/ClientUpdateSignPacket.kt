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
    val lines: Array<String> = Array(4) { "" }

    init {
        val input = getIStream()

        blockPos = input.readBlockPos()
        for(i in 0 until 3) {
            lines[i] = input.readString()
        }
    }
}
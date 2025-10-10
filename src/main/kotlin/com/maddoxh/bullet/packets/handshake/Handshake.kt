package com.maddoxh.bullet.packets.handshake

import com.maddoxh.bullet.types.String.readString
import com.maddoxh.bullet.types.VarInt.readVarInt
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort

data class Handshake(
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
) {
    companion object {
        const val PACKET_ID = 0x00

        suspend fun readFrom(input: ByteReadPacket): Handshake {
            val protocolVersion = input.readVarInt()
            val address = input.readString()
            val port = input.readShort().toInt() and 0xFFFF
            val nextState = input.readVarInt()
            return Handshake(protocolVersion, address, port, nextState)
        }
    }

    object State {
        const val STATUS = 1
        const val LOGIN = 2
    }
}

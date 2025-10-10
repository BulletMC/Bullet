package com.maddoxh.bullet.packets.handshake

import com.maddoxh.bullet.TypeHelpers
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
            val protocolVersion = TypeHelpers.readVarInt(input)
            val address = TypeHelpers.readString(input)
            val port = input.readShort().toInt() and 0xFFFF
            val nextState = TypeHelpers.readVarInt(input)
            return Handshake(protocolVersion, address, port, nextState)
        }
    }

    object State {
        const val STATUS = 1
        const val LOGIN = 2
    }
}

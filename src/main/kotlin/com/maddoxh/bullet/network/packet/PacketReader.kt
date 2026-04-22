package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.HandshakeIntention
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.PingRequest
import com.maddoxh.bullet.network.packet.impl.`in`.StatusRequest
import com.maddoxh.bullet.state.ConnectionState

object PacketReader {
    fun read(
        state: ConnectionState,
        packetID: Int,
        payloadLength: Int,
        input: MinecraftInputStream
    ): InboundPacket? = when(state) {
        ConnectionState.HANDSHAKE -> readHandshake(packetID, input)
        ConnectionState.STATUS -> readStatus(packetID, payloadLength, input)
        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }

    private fun readHandshake(packetID: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> HandshakeIntention(
            protocolVersion = input.readVarInt(),
            serverAddress = input.readMCString(),
            serverPort = input.readUnsignedShort(),
            nextState = input.readVarInt()
        )

        else -> null
    }

    private fun readStatus(packetID: Int, payloadLength: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> StatusRequest
        0x01 -> PingRequest(payload = input.readLong())

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }
}

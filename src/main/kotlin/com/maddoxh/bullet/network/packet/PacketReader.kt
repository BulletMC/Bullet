package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.state.ConnectionState

object PacketReader {
    fun read(
        state: ConnectionState,
        packetID: Int,
        payloadLength: Int,
        input: MinecraftInputStream
    ): Packet? = when(state) {
        ConnectionState.HANDSHAKE -> readHandshake(packetID, input)
        ConnectionState.STATUS -> readStatus(packetID, payloadLength, input)
        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }

    private fun readHandshake(packetID: Int, input: MinecraftInputStream): Packet? = when(packetID) {
        0x00 -> Packet.Handshake.Intention(
            protocolVersion = input.readVarInt(),
            serverAddress = input.readMCString(),
            serverPort = input.readUnsignedShort(),
            nextState = input.readVarInt()
        )

        else -> null
    }

    private fun readStatus(packetID: Int, payloadLength: Int, input: MinecraftInputStream): Packet? = when(packetID) {
        0x00 -> Packet.Status.StatusRequest
        0x01 -> Packet.Status.PingRequest(payload = input.readLong())

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }
}
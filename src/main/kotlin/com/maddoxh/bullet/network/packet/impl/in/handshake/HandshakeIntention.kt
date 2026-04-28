package com.maddoxh.bullet.network.packet.impl.`in`.handshake

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.HandshakeInboundPacket

data class HandshakeIntention( // 0x00 C->S
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
) : HandshakeInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream) = HandshakeIntention(
            protocolVersion = input.readVarInt(),
            serverAddress   = input.readMCString(),
            serverPort      = input.readUnsignedShort(),
            nextState       = input.readVarInt()
        )
    }
}

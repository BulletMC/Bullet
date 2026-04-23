package com.maddoxh.bullet.network.packet.impl.`in`.handshake

import com.maddoxh.bullet.network.packet.impl.`in`.HandshakeInboundPacket

data class HandshakeIntention( // 0x00 C->S
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
) : HandshakeInboundPacket
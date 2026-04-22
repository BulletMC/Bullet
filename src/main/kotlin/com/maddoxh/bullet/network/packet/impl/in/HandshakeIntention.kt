package com.maddoxh.bullet.network.packet.impl.`in`

data class HandshakeIntention( // 0x00 C->S
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Int,
    val nextState: Int
) : InboundPacket

package com.maddoxh.bullet.network.packet.impl.`in`

data class PingRequest(val payload: Long) : InboundPacket // 0x01 C->S

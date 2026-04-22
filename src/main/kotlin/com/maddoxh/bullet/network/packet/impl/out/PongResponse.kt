package com.maddoxh.bullet.network.packet.impl.out

data class PongResponse(val payload: Long) : OutboundPacket // 0x01 S->C

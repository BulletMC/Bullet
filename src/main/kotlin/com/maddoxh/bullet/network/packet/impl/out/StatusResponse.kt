package com.maddoxh.bullet.network.packet.impl.out

data class StatusResponse(val json: String) : OutboundPacket // 0x00 S->C

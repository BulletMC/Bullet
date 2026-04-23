package com.maddoxh.bullet.network.packet.impl.out.status

import com.maddoxh.bullet.network.packet.impl.out.StatusOutboundPacket

data class StatusResponse(val json: String) : StatusOutboundPacket // 0x00 S->C

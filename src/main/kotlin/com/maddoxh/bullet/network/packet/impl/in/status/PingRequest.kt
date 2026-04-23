package com.maddoxh.bullet.network.packet.impl.`in`.status

import com.maddoxh.bullet.network.packet.impl.`in`.StatusInboundPacket

data class PingRequest(val payload: Long) : StatusInboundPacket // 0x01 C->S

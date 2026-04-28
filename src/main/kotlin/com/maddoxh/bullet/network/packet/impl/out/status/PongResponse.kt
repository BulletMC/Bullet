package com.maddoxh.bullet.network.packet.impl.out.status

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.StatusOutboundPacket

data class PongResponse(val payload: Long) : StatusOutboundPacket { // 0x01 S->C
    override val packetId = 0x01
    override fun encode() = MinecraftOutputStream.build { writeLong(payload) }
}

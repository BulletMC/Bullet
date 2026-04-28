package com.maddoxh.bullet.network.packet.impl.out.status

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.StatusOutboundPacket

data class StatusResponse(val json: String) : StatusOutboundPacket { // 0x00 S->C
    override val packetId = 0x00
    override fun encode() = MinecraftOutputStream.build { writeMCString(json) }
}

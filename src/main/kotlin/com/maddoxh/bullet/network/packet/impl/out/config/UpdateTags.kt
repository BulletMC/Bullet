package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

object UpdateTags : ConfigOutboundPacket { // 0x0D S->C
    override val packetId = 0x0D
    override fun encode() = MinecraftOutputStream.build { writeVarInt(0) }
}

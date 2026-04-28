package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

object FeatureFlags : ConfigOutboundPacket { // 0x0C S->C
    override val packetId = 0x0C
    override fun encode() = MinecraftOutputStream.build {
        writeVarInt(1)
        writeMCString("minecraft:vanilla")
    }
}

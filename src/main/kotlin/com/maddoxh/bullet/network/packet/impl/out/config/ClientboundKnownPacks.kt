package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

object ClientboundKnownPacks : ConfigOutboundPacket { // 0x0E S->C
    override val packetId = 0x0E
    override fun encode() = MinecraftOutputStream.build {
        writeVarInt(1)
        writeMCString("minecraft")
        writeMCString("core")
        writeMCString("1.21.4")
    }
}

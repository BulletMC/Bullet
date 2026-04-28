package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

data class ConfigDisconnect(val reason: String) : ConfigOutboundPacket { // 0x02 S->C
    override val packetId = 0x02
    override fun encode() = MinecraftOutputStream.build { writeMCString(reason) }
}

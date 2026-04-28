package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

data class ClientboundPluginMessage( // 0x01 S->C
    val channel: String,
    val data: ByteArray
) : ConfigOutboundPacket {
    override val packetId = 0x01
    override fun encode() = MinecraftOutputStream.build {
        writeMCString(channel)
        write(data)
    }
}

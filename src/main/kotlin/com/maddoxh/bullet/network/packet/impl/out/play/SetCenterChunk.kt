package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class SetCenterChunk(
    val chunkX: Int,
    val chunkZ: Int,
) : PlayOutboundPacket {
    override val packetId = 0x54
    override fun encode() = MinecraftOutputStream.build {
        writeVarInt(chunkX)
        writeVarInt(chunkZ)
    }
}

package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class GameEvent( // 0x22 S->C
    val event: Int,
    val value: Float = 0.0f
) : PlayOutboundPacket {
    override val packetId = 0x22
    override fun encode() = MinecraftOutputStream.build {
        writeByte(event)
        writeFloat(value)
    }

    companion object {
        const val START_WAITING_FOR_CHUNKS = 13
    }
}

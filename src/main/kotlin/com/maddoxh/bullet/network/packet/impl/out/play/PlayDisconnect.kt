package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class PlayDisconnect(val reason: String) : PlayOutboundPacket { // 0x1B S->C
    override val packetId = 0x1B
    override fun encode() = MinecraftOutputStream.build { writeMCString(reason) }
}

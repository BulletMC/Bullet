package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class SetHeldItem(val slot: Int = 0) : PlayOutboundPacket { // 0x53 S->C
    override val packetId = 0x53
    override fun encode() = MinecraftOutputStream.build { writeByte(slot) }
}

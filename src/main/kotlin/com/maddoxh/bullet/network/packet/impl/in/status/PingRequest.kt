package com.maddoxh.bullet.network.packet.impl.`in`.status

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.StatusInboundPacket

data class PingRequest(val payload: Long) : StatusInboundPacket { // 0x01 C->S
    companion object {
        fun decode(input: MinecraftInputStream) = PingRequest(input.readLong())
    }
}

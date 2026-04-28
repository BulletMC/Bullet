package com.maddoxh.bullet.network.packet.impl.`in`.play

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.PlayInboundPacket

data class ConfirmTeleportation( // 0x00 C->S
    val teleportID: Int
) : PlayInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream) = ConfirmTeleportation(
            teleportID = input.readVarInt()
        )
    }
}

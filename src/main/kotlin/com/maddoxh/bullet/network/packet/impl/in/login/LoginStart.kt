package com.maddoxh.bullet.network.packet.impl.`in`.login

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.LoginInboundPacket
import java.util.UUID

data class LoginStart( // 0x00 C->S
    val name: String,
    val uuid: UUID
) : LoginInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream) = LoginStart(
            name = input.readMCString(),
            uuid = UUID(input.readLong(), input.readLong())
        )
    }
}

package com.maddoxh.bullet.network.packet.impl.out.login

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket
import java.util.UUID

data class LoginSuccess( // 0x02 S->C
    val uuid: UUID,
    val username: String
) : LoginOutboundPacket {
    override val packetId = 0x02
    override fun encode() = MinecraftOutputStream.build {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
        writeMCString(username)
        writeVarInt(0)
    }
}

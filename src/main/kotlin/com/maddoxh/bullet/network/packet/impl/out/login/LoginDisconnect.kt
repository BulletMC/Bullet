package com.maddoxh.bullet.network.packet.impl.out.login

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket

data class LoginDisconnect(val reason: String) : LoginOutboundPacket { // 0x00 S->C
    override val packetId = 0x00
    override fun encode() = MinecraftOutputStream.build { writeMCString(reason) }
}

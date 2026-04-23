package com.maddoxh.bullet.network.packet.impl.`in`.login

import com.maddoxh.bullet.network.packet.impl.`in`.LoginInboundPacket
import java.util.UUID

data class LoginStart(
    val name: String,
    val uuid: UUID
) : LoginInboundPacket // 0x00 C->S

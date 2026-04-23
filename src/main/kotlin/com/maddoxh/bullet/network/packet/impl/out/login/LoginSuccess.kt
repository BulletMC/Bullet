package com.maddoxh.bullet.network.packet.impl.out.login

import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket
import java.util.UUID

data class LoginSuccess( // 0x02 S->C
    val uuid: UUID,
    val username: String
) : LoginOutboundPacket

package com.maddoxh.bullet.network.packet.impl.out.login

import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket

data class LoginDisconnect( // 0x00 S->C
    val reason: String
) : LoginOutboundPacket

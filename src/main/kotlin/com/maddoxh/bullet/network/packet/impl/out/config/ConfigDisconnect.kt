package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket

data class ConfigDisconnect( // 0x02 S->C
    val reason: String
) : LoginOutboundPacket

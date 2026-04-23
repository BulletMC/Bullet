package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.network.packet.impl.out.LoginOutboundPacket

data class PlayDisconnect( // 0x1B S->C
    val reason: String
) : LoginOutboundPacket

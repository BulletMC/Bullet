package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

data class ClientboundPluginMessage( // 0x01 S->C
    val channel: String,
    val data: ByteArray
) : ConfigOutboundPacket

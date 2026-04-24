package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class ServerboundPluginMessage( // 0x02 C->S
    val channel: String,
    val data: ByteArray
) : ConfigInboundPacket

package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class KnownPack(
    val namespace: String,
    val id: String,
    val version: String
)

data class ServerboundKnownPacks( // 0x07 C->S
    val packs: List<KnownPack>
) : ConfigInboundPacket

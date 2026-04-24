package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class ClientInformation( // 0x00 C->S
    val locale: String,
    val viewDistance: Byte,
    val chatMode: Int,
    val chatColors: Boolean,
    val displayedSkinParts: Int,
    val mainHand: Int,
    val enableTextFiltering: Boolean,
    val allowServerListings: Boolean,
    val particleStatus: Int
) : ConfigInboundPacket

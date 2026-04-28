package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket

object FinishConfiguration : ConfigOutboundPacket { // 0x03 S->C
    override val packetId = 0x03
    override fun encode() = ByteArray(0)
}

package com.maddoxh.bullet.network.packet.impl.out

sealed interface OutboundPacket {
    val packetId: Int
    fun encode(): ByteArray
}

interface StatusOutboundPacket : OutboundPacket
interface LoginOutboundPacket : OutboundPacket
interface ConfigOutboundPacket : OutboundPacket
interface PlayOutboundPacket : OutboundPacket

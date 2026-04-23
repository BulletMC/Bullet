package com.maddoxh.bullet.network.packet.impl.`in`

sealed interface InboundPacket

interface HandshakeInboundPacket : InboundPacket
interface StatusInboundPacket : InboundPacket
interface LoginInboundPacket : InboundPacket
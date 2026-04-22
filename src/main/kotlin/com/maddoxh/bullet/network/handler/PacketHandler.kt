package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket

interface PacketHandler {
    fun handle(packet: InboundPacket, connection: ClientConnection)
}

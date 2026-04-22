package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.Packet

interface PacketHandler {
    fun handle(packet: Packet, connection: ClientConnection)
}
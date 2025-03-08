package com.aznos.packets.play.out

import com.aznos.packets.Packet

class ServerChangeGameStatePacket(
    private val reason: Int,
    private val value: Float
) : Packet(0x1D) {
    init {
        wrapper.writeByte(reason)
        wrapper.writeFloat(value)
    }
}
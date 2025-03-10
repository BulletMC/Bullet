package com.aznos.packets.play.out

import com.aznos.packets.Packet

class ServerChangeGameStatePacket(
    reason: Int,
    value: Float
) : Packet(0x1D) {
    init {
        wrapper.writeByte(reason)
        wrapper.writeFloat(value)
    }
}
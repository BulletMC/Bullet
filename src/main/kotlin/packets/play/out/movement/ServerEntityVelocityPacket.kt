package com.aznos.packets.play.out.movement

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

class ServerEntityVelocityPacket(
    entityID: Int,
    x: Short,
    y: Short,
    z: Short
) : Packet(0x46) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeShort(x.toInt())
        wrapper.writeShort(y.toInt())
        wrapper.writeShort(z.toInt())
    }
}
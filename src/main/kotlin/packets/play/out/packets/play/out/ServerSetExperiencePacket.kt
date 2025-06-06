package com.aznos.packets.play.out.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server when the client should change experience levels
 */
class ServerSetExperiencePacket(
    experienceBar: Float,
    level: Int,
    totalXP: Int
) : Packet(0x48) {
    init {
        wrapper.writeFloat(experienceBar)
        wrapper.writeVarInt(level)
        wrapper.writeVarInt(totalXP)
    }
}
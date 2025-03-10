package com.aznos.packets.play.out.movement

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent to all clients when a player rotates their head
 *
 * @param entityID The entity ID of the player
 * @param yaw The yaw of the player
 */
class ServerEntityHeadLook(
    entityID: Int,
    yaw: Float
) : Packet(0x3A) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeByte((yaw * 256.0f / 360.0f).toInt())
    }
}
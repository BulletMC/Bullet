package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Location
import com.aznos.packets.Packet

/**
 * This packet is sent instead of an entity movement packet when an entity
 * moves more than 8 blocks
 */
class ServerEntityTeleportPacket(
    entityID: Int,
    location: Location,
    onGround: Boolean
) : Packet(0x56) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeDouble(location.x)
        wrapper.writeDouble(location.y)
        wrapper.writeDouble(location.z)
        wrapper.writeByte((location.yaw * 256.0f / 360.0f).toInt())
        wrapper.writeByte((location.pitch * 256.0f / 360.0f).toInt())
        wrapper.writeBoolean(onGround)
    }
}
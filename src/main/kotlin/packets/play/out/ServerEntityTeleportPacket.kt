package com.aznos.packets.play.out

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocation
import com.aznos.datatypes.LocationType.writeLocationAngle
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent instead of an entity movement packet when an entity
 * moves more than 8 blocks
 */
class ServerEntityTeleportPacket(
    entityID: Int,
    location: LocationType.Location,
    onGround: Boolean
) : Packet(0x56) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeLocationAngle(location)
        wrapper.writeBoolean(onGround)
    }
}
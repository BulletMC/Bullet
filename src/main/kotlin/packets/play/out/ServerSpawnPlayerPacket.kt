package com.aznos.packets.play.out

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocationAngle
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import java.util.UUID

/**
 * This packet is sent to all clients to inform them that a new player has spawned
 */
class ServerSpawnPlayerPacket(
    entityID: Int,
    uuid: UUID,
    location: LocationType.Location
) : Packet(0x04) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeUUID(uuid)
        wrapper.writeLocationAngle(location)
    }
}
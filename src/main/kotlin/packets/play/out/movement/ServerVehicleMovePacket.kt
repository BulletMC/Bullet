package com.aznos.packets.play.out.movement

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocation
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server when a vehicle moves
 */
class ServerVehicleMovePacket(
    location: LocationType.Location
) : Packet(0x2B) {
    init {
        wrapper.writeLocation(location)
    }
}
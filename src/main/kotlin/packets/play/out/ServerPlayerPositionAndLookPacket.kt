package com.aznos.packets.play.out

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocation
import com.aznos.packets.Packet

/**
 * Sends the player position and look packet to the client
 * this is the last packet needed in order for the client to join the world
 *
 * @param location The new location
 */
class ServerPlayerPositionAndLookPacket(
    location: LocationType.Location
) : Packet(0x34) {
    init {
        wrapper.writeLocation(location)

        wrapper.writeByte(0)
        wrapper.writeByte(0)
    }
}
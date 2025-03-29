package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Set the passengers of a vehicle
 *
 * @param entityID The vehicle's entity ID
 * @param passengers The passengers entity IDs
 */
class ServerSetPassengersPacket(
    entityID: Int,
    passengers: List<Int>
) : Packet(0x4B) {
    init {
        wrapper.writeVarInt(entityID)

        wrapper.writeVarInt(passengers.size)
        for(passenger in passengers) {
            wrapper.writeVarInt(passenger)
        }
    }
}
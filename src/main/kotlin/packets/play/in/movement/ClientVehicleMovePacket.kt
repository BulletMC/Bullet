package com.aznos.packets.play.`in`.movement

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.readLocation
import com.aznos.packets.Packet

/**
 * Sent when a player moves in a vehicle
 *
 * @property location The new location of the vehicle
 */
class ClientVehicleMovePacket(data: ByteArray) : Packet(data) {
    val location: LocationType.Location = getIStream().readLocation()
}
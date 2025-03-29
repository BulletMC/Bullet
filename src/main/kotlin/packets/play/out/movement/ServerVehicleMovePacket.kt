package com.aznos.packets.play.out.movement

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocation
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server when a vehicle moves
 */
class ServerVehicleMovePacket(
    x: Double,
    y: Double,
    z: Double,
    yaw: Float,
    pitch: Float,
) : Packet(0x2B) {
    init {
        wrapper.writeDouble(x)
        wrapper.writeDouble(y)
        wrapper.writeDouble(z)
        wrapper.writeFloat(yaw)
        wrapper.writeFloat(pitch)
    }
}
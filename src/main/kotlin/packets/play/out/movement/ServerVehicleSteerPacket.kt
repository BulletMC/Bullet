package com.aznos.packets.play.out.movement

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocation
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server when a player steers a vehicle
 *
 * @param sideways The sideways movement of the vehicle, positive to the left
 * of the player
 * @param forward Positive forward
 * @param flags Bit mask of flags, 0x01: jump, 0x02: unmount
 */
class ServerVehicleSteerPacket(
    sideways: Float,
    forward: Float,
    flags: Byte
) : Packet(0x1D) {
    init {
        println("sent vehicle steer packet")
        wrapper.writeFloat(sideways)
        wrapper.writeFloat(forward)
        wrapper.writeByte(flags.toInt())
    }
}
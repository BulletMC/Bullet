package com.aznos.packets.play.`in`.movement

import com.aznos.packets.Packet

/**
 * Sent when a player moves in a vehicle
 *
 * @property x The x coordinate of the vehicle
 * @property y The y coordinate of the vehicle
 * @property z The z coordinate of the vehicle
 * @property yaw The yaw of the vehicle
 * @property pitch The pitch of the vehicle
 */
class ClientVehicleMovePacket(data: ByteArray) : Packet(data) {
    val x: Double
    val y: Double
    val z: Double
    val yaw: Float
    val pitch: Float

    init {
        val input = getIStream()

        x = input.readDouble()
        y = input.readDouble()
        z = input.readDouble()
        yaw = input.readFloat()
        pitch = input.readFloat()
    }
}
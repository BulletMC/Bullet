package com.aznos.packets.play.`in`.movement

import com.aznos.packets.Packet

/**
 * Sent when a player moves in a vehicle
 *
 * @property sideways Positive to the left of the player
 * @property forward Positive forward
 * @property flags 0x01 if jumping, 0x02 for unmounting
 */
class ClientSteerVehiclePacket(data: ByteArray) : Packet(data) {
    val sideways: Float
    val forward: Float
    val flags: Byte

    init {
        val input = getIStream()

        sideways = input.readFloat()
        forward = input.readFloat()
        flags = input.readByte()
    }
}
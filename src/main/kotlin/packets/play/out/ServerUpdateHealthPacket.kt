package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent by the server to update/set the health or food of the player it's sent to
 *
 * @param health 0 or less = dead, 20 = full HP
 * @param food 0-20
 * @param foodSaturation Seems to be 0.0-5.0 in integer increments
 */
class ServerUpdateHealthPacket(
    health: Float,
    food: Int,
    foodSaturation: Float
) : Packet(0x49) {
    init {
        wrapper.writeFloat(health)
        wrapper.writeVarInt(food)
        wrapper.writeFloat(foodSaturation)
    }
}
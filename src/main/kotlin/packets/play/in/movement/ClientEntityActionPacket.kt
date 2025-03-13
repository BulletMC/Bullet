package com.aznos.packets.play.`in`.movement

import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent to indicate that the client has performed certain actions
 * including sneaking, sprinting, jumping with a horse, etc
 *
 * Action IDs:
 * 0 - Start sneaking
 * 1 - Stop sneaking
 * 2 - Leave bed
 * 3 - Start sprinting
 * 4 - Stop sprinting
 * 5 - Start jump with horse
 * 6 - Stop jump with horse
 * 7 - Open horse inventory
 * 8 - Start flying with elytra
 *
 * Leave bed is only sent when the "Leave bed" button is clicked
 * not when waking up naturally
 *
 * @property entityID The ID of the entity performing the action
 * @property actionID The ID of the action, see above
 * @property jumpBoost Only used by the "start jump with horse" action, 0-100
 */
class ClientEntityActionPacket(data: ByteArray) : Packet(data) {
    val entityID: Int
    val actionID: Int
    var jumpBoost: Int = 0

    init {
        val input = getIStream()

        entityID = input.readVarInt()
        actionID = input.readVarInt()
        jumpBoost = input.readVarInt()
    }
}
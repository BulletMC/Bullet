package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent from the client to the server when the client attacks
 * or right clicks another entity
 *
 * The server only accepts this packet if the entity being attached/used is
 * visible without obstruction and within a 4 unit radius of the players position
 *
 * @property entityID The ID of the entity being interacted with
 * @property type The type of interaction, 0: Interact, 1: Attack, 2: Interact At
 * @property targetX Only if type is interact at
 * @property targetY Only if type is interact at
 * @property targetZ Only if type is interact at
 * @property hand Only if type is interact or interact at, 0: Main hand, 1: Off-hand
 * @property sneaking If the player is sneaking
 */
class ClientInteractEntityPacket(data: ByteArray) : Packet(data) {
    val entityID: Int
    val type: Int
    val targetX: Float?
    val targetY: Float?
    val targetZ: Float?
    val hand: Int?
    val sneaking: Boolean

    init {
        val input = getIStream()

        entityID = input.readVarInt()
        type = input.readVarInt()

        if(type == 2) {
            targetX = input.readFloat()
            targetY = input.readFloat()
            targetZ = input.readFloat()
        } else {
            targetX = null
            targetY = null
            targetZ = null
        }

        if(type == 0 || type == 2) {
            hand = input.readVarInt()
        } else {
            hand = null
        }

        sneaking = input.readBoolean()
    }
}
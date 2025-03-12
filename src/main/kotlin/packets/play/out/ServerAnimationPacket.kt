package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent to all clients to play an animation on another entity
 *
 * @param entityID The ID of the entity to play the animation on
 * @param animation The animation to play
 *
 * Available animations:
 * 0 - Swing main arm
 * 1 - Take damage
 * 2 - Leave bed
 * 3 - Swing offhand
 * 4 - Critical effect
 * 5 - Magic critical effect
 */
class ServerAnimationPacket(
    entityID: Int,
    animation: Int
) : Packet(0x05) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeByte(animation)
    }
}
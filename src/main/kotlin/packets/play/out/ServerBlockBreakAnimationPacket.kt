package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Sent by the server to play a block break animation
 *
 * @param entityID The entity ID of the entity breaking the block
 * @param location The location of the block being broken
 * @param stage The stage of the block break animation (0-9)
 */
class ServerBlockBreakAnimationPacket(
    entityID: Int,
    location: Position,
    stage: Int
) : Packet(0x08) {
    init {
        wrapper.writeVarInt(entityID)

        val blockPosition: Long = ((location.x and 0x3FFFFFF).toLong() shl 38) or
                ((location.z and 0x3FFFFFF).toLong() shl 12) or
                (location.y and 0xFFF).toLong()

        wrapper.writeLong(blockPosition)
        wrapper.writeByte(stage)
    }
}
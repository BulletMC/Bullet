package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeVanillaBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Spawns one or more experience orbs
 *
 * @param entityID The unique ID of the experience orb entity
 * @param blockPos Where to spawn the experience orb
 * @param count The amount of experience this orb will reward once collected
 */
class ServerSpawnExperienceOrb(
    entityID: Int,
    blockPos: BlockPositionType.BlockPosition,
    count: Int
) : Packet(0x01) {
    init {
        wrapper.writeVarInt(entityID);
        wrapper.writeVanillaBlockPos(blockPos);
        wrapper.writeShort(count);
    }
}
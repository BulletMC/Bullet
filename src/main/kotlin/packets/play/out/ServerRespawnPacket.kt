package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.entity.player.data.GameMode
import com.aznos.packets.Packet
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag

/**
 * Sent to the client to respawn the player
 *
 * To change the player dimension, send them a respawn packet with the
 *  appropriate dimension data, followed by prechunks/chunks for the new
 * dimension, and finally send a position and look packet. You do not need to
 * unload chunks, the client does it automatically
 *
 * @param codec The dimension codec, same as in the join game packet
 * @param worldName The name of the world being spawned into
 * @param gameMode The players gamemode
 * @param isDebug Debug mode worlds cannot be modified and have predefined blocks
 * @param isFlat If the world is a superflat world
 * @param copyMetadata If false, metadata is reset on the respawned player
 */
class ServerRespawnPacket(
    codec: CompoundTag,
    worldName: String,
    gameMode: GameMode,
    isDebug: Boolean,
    isFlat: Boolean,
    copyMetadata: Boolean
) : Packet(0x39) {
    init {
        val nbt = Nbt()

        val dimensionTypeList = codec.getCompound("minecraft:dimension_type")
            .getList<CompoundTag>("value")

        val overworldEntry = dimensionTypeList.first {
            it.getString("name").value == "minecraft:overworld"
        }

        val dimension = overworldEntry.getCompound("element")
        nbt.toStream(dimension, wrapper)

        wrapper.writeString(worldName)
        wrapper.writeLong(0)
        wrapper.writeByte(gameMode.id)
        wrapper.writeByte(1)
        wrapper.writeBoolean(isDebug)
        wrapper.writeBoolean(isFlat)
        wrapper.writeBoolean(copyMetadata)
    }
}
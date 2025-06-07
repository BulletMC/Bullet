package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.entity.player.data.GameMode
import com.aznos.serialization.NBTJson
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.Tag
import java.io.ByteArrayOutputStream

/**
 * Packet sent to the client that sends all the information needed to join the game
 *
 * @param entityID The players entity id
 * @param hardcore If the world is hardcore or not
 * @param gameMode The players gamemode
 * @param world The world name
 * @param codec The dimension codec
 * @param maxPlayers The maximum number of players that can join the world
 * @param viewDistance The view distance set by the server
 * @param reducedDebugInfo Whether to have reduced debug info
 * @param isDebug Whether the server is on debug ode
 * @param isFlat Whether the world is flat or not
 */
class ServerJoinGamePacket(
    entityID: Int,
    hardcore: Boolean,
    gameMode: GameMode,
    world: String,
    codec: CompoundTag,
    maxPlayers: Int,
    viewDistance: Int,
    reducedDebugInfo: Boolean,
    enableRespawnScreen: Boolean,
    isDebug: Boolean,
    isFlat: Boolean
) : Packet(0x24) {
    init {
        wrapper.writeInt(entityID)
        wrapper.writeBoolean(hardcore)
        wrapper.writeByte(gameMode.id)
        wrapper.writeByte(-1)
        wrapper.writeVarInt(1)
        wrapper.writeString(world)

        wrapper.write(NBTJson.toNBTBytes(codec))

        val dimensionTypeRoot   = codec.getCompoundTag("minecraft:dimension_type")
        val dimensionTypeValues = dimensionTypeRoot.getListTag("value")

        val overworld = dimensionTypeValues
            .firstOrNull { it is CompoundTag && it.getString("name") == "minecraft:overworld" }
            ?.let { (it as CompoundTag).getCompoundTag("element") }
            ?: error("minecraft:overworld dimension missing from codec")

        wrapper.write(NBTJson.toNBTBytes(overworld))

        wrapper.writeString(world)
        wrapper.writeLong(0)
        wrapper.writeVarInt(maxPlayers)
        wrapper.writeVarInt(viewDistance)
        wrapper.writeBoolean(reducedDebugInfo)
        wrapper.writeBoolean(enableRespawnScreen)
        wrapper.writeBoolean(isDebug)
        wrapper.writeBoolean(isFlat)
    }
}

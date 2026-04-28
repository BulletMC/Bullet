package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.entity.player.GameMode
import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class LoginPlay( // 0x2B S->C
    val entityID: Int,
    val isHardcore: Boolean = false,
    val gameMode: GameMode = GameMode.CREATIVE,
    val previousGameMode: Int = -1,
    val dimensionNames: List<String> = listOf("minecraft:overworld"),
    val dimensionType: Int = 0,
    val dimensionName: String = "minecraft:overworld",
    val hashedSeed: Long = 0L,
    val maxPlayers: Int = 100,
    val viewDistance: Int = 8,
    val simulationDistance: Int = 8,
    val reducedDebugInfo: Boolean = false,
    val enableRespawnScreen: Boolean = true,
    val doLimitedCrafting: Boolean = false,
    val portalCooldown: Int = 0,
    val seaLevel: Int = 64,
    val isFlat: Boolean = true,
    val hasDeathLocation: Boolean = false,
    val enforcesSecureChat: Boolean = false
) : PlayOutboundPacket {
    override val packetId = 0x2B
    override fun encode() = MinecraftOutputStream.build {
        writeInt(entityID)
        writeBoolean(isHardcore)
        writeVarInt(dimensionNames.size)
        dimensionNames.forEach { writeMCString(it) }
        writeVarInt(maxPlayers)
        writeVarInt(viewDistance)
        writeVarInt(simulationDistance)
        writeBoolean(reducedDebugInfo)
        writeBoolean(enableRespawnScreen)
        writeBoolean(doLimitedCrafting)
        writeVarInt(dimensionType)
        writeMCString(dimensionName)
        writeLong(hashedSeed)
        writeByte(gameMode.id)
        writeByte(previousGameMode)
        writeBoolean(false)
        writeBoolean(isFlat)
        writeBoolean(hasDeathLocation)
        writeVarInt(portalCooldown)
        writeVarInt(seaLevel)
        writeBoolean(enforcesSecureChat)
    }
}

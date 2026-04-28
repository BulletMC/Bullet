package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class KnownPack(
    val namespace: String,
    val id: String,
    val version: String
)

data class ServerboundKnownPacks( // 0x07 C->S
    val packs: List<KnownPack>
) : ConfigInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream): ServerboundKnownPacks {
            val count = input.readVarInt()
            val packs = (0 until count).map {
                KnownPack(
                    namespace = input.readMCString(),
                    id        = input.readMCString(),
                    version   = input.readMCString()
                )
            }
            return ServerboundKnownPacks(packs)
        }
    }
}

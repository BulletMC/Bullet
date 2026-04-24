package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket
import net.kyori.adventure.nbt.CompoundBinaryTag

data class RegistryEntry(
    val id: String,
    val data: CompoundBinaryTag?
)

data class RegistryData( // 0x07 S->C
    val registryID: String,
    val entries: List<RegistryEntry>
) : ConfigOutboundPacket

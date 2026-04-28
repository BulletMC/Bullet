package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class ClientInformation( // 0x00 C->S
    val locale: String,
    val viewDistance: Byte,
    val chatMode: Int,
    val chatColors: Boolean,
    val displayedSkinParts: Int,
    val mainHand: Int,
    val enableTextFiltering: Boolean,
    val allowServerListings: Boolean,
    val particleStatus: Int
) : ConfigInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream) = ClientInformation(
            locale              = input.readMCString(),
            viewDistance        = input.readByte(),
            chatMode            = input.readVarInt(),
            chatColors          = input.readBoolean(),
            displayedSkinParts  = input.readUnsignedByte(),
            mainHand            = input.readVarInt(),
            enableTextFiltering = input.readBoolean(),
            allowServerListings = input.readBoolean(),
            particleStatus      = input.readVarInt()
        )
    }
}

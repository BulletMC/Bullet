package com.maddoxh.bullet.network.packet.impl.`in`.config

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.io.VarInt
import com.maddoxh.bullet.network.packet.impl.`in`.ConfigInboundPacket

data class ServerboundPluginMessage( // 0x02 C->S
    val channel: String,
    val data: ByteArray
) : ConfigInboundPacket {
    companion object {
        fun decode(input: MinecraftInputStream, payloadLen: Int): ServerboundPluginMessage {
            val channel = input.readMCString()
            val dataLen = payloadLen - channel.toByteArray().size - VarInt.varIntSize(channel.toByteArray().size)
            val data = ByteArray(dataLen.coerceAtLeast(0))
            if(data.isNotEmpty()) input.readFully(data)
            return ServerboundPluginMessage(channel, data)
        }
    }
}

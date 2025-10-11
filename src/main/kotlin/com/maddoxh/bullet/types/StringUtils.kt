package com.maddoxh.bullet.types

import com.maddoxh.bullet.types.VarInt.readVarInt
import com.maddoxh.bullet.types.VarInt.writeVarInt
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.*

object StringUtils {
    suspend fun ByteReadChannel.readString(): String {
        val length = readVarInt()
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }

    suspend fun ByteReadPacket.readString(): String {
        val length = readVarInt()
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }

    fun BytePacketBuilder.writeString(value: String) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        writeVarInt(bytes.size)
        writeFully(bytes, 0, bytes.size)
    }
}
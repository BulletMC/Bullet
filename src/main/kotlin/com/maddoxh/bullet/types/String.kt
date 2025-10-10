package com.maddoxh.bullet.types

import com.maddoxh.bullet.types.VarInt.readVarInt
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readFully

object String {
    suspend fun ByteReadChannel.readString(): kotlin.String {
        val length = readVarInt()
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }

    suspend fun ByteReadPacket.readString(): kotlin.String {
        val length = readVarInt()
        val bytes = ByteArray(length)
        readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }
}
package com.maddoxh.bullet

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readFully

object TypeHelpers {
    suspend fun readVarInt(input: ByteReadChannel): Int {
        var numRead = 0
        var result = 0
        while(true) {
            val read = input.readByte().toInt() and 0xFF
            result = result or ((read and 0x7F) shl (7 * numRead))
            numRead++
            if(numRead > 5) error("VarInt is too big")
            if((read and 0x80) == 0) break
        }

        return result
    }

    suspend fun readString(input: ByteReadChannel): String {
        val length = readVarInt(input)
        val bytes = ByteArray(length)
        input.readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }

    fun readVarInt(packet: ByteReadPacket): Int {
        var numRead = 0
        var result = 0
        while(true) {
            val read = packet.readByte().toInt() and 0xFF
            result = result or ((read and 0x7F) shl (7 * numRead))
            numRead++
            if(numRead > 5) error("VarInt is too big")
            if((read and 0x80) == 0) break
        }

        return result
    }

    fun readString(packet: ByteReadPacket): String {
        val length = readVarInt(packet)
        val bytes = ByteArray(length)
        packet.readFully(bytes, 0, length)
        return String(bytes, Charsets.UTF_8)
    }
}
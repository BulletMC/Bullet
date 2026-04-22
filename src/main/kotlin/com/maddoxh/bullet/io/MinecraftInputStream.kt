package com.maddoxh.bullet.io

import java.io.DataInputStream
import java.io.InputStream

class MinecraftInputStream(stream: InputStream) : DataInputStream(stream) {
    fun readVarInt(): Int {
        var value = 0
        var position = 0
        var currentByte: Byte

        while (true) {
            currentByte = readByte()
            value = value or ((currentByte.toInt() and 0x7F) shl position)

            if((currentByte.toInt() and 0x80) == 0) break

            position += 7
            if(position >= 32) throw RuntimeException("VarInt too large")
        }

        return value
    }

    fun readVarLong(): Long {
        var value = 0L
        var position = 0
        var currentByte: Byte

        while(true) {
            currentByte = readByte()
            value = value or ((currentByte.toLong() and 0x7FL) shl position)

            if((currentByte.toInt() and 0x80) == 0) break

            position += 7
            if(position >= 64) throw RuntimeException("VarLong too large")
        }

        return value
    }

    fun readMCString(): String {
        val length = readVarInt()
        val bytes = ByteArray(length)
        readFully(bytes)
        return String(bytes, Charsets.UTF_8)
    }
}
package com.maddoxh.bullet.types

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket

object VarInt {
    suspend fun ByteReadPacket.readVarInt(): Int {
        var numRead = 0
        var result = 0
        while(true) {
            val read = readByte().toInt() and 0xFF
            result = result or ((read and 0x7F) shl (7 * numRead))
            numRead++
            if (numRead > 5) error("VarInt is too big")
            if ((read and 0x80) == 0) break
        }

        return result
    }

    suspend fun ByteReadChannel.readVarInt(): Int {
        var numRead = 0
        var result = 0
        while(true) {
            val read = this.readByte().toInt() and 0xFF
            result = result or ((read and 0x7F) shl (7 * numRead))
            numRead++
            if(numRead > 5) error("VarInt is too big")
            if((read and 0x80) == 0) break
        }

        return result
    }

    fun BytePacketBuilder.writeVarInt(value: Int) {
        var v = value
        while(true) {
            if((v and 0x7F.inv()) == 0) {
                writeByte(v.toByte()); return
            }

            writeByte(((v and 0x7F) or 0x80).toByte())
            v = v ushr 7
        }
    }
}
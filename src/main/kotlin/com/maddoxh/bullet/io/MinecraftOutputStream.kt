package com.maddoxh.bullet.io

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.OutputStream

class MinecraftOutputStream(stream: OutputStream) : DataOutputStream(stream) {
    fun writeVarInt(value: Int) {
        var v = value
        while(true) {
            if((v and 0x7F.inv()) == 0) {
                writeByte(v)
                return
            }

            writeByte((v and 0x7F) or 0x80)
            v = v ushr 7
        }
    }

    fun writeVarLong(value: Long) {
        var v = value
        while(true) {
            if((v and 0x7FL.inv()) == 0L) {
                writeByte(v.toInt())
                return
            }

            writeByte(((v and 0x7FL) or 0x80L).toInt())
            v = v ushr 7
        }
    }

    fun writeMCString(value: String) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        writeVarInt(bytes.size)
        write(bytes)
    }

    fun writePacket(packetId: Int, payload: ByteArray) {
        val idBuf = ByteArrayOutputStream()
        MinecraftOutputStream(idBuf).writeVarInt(packetId)
        val idBytes = idBuf.toByteArray()

        writeVarInt(idBytes.size + payload.size)
        write(idBytes)
        write(payload)
    }
}
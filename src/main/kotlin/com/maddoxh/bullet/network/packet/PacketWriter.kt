package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.io.MinecraftOutputStream
import java.io.ByteArrayOutputStream

object PacketWriter {
    fun serialize(packet: Packet): Pair<Int, ByteArray> = when(packet) {
        is Packet.Status.StatusResponse -> 0x00 to encodeStatusResponse(packet)
        is Packet.Status.PongResponse   -> 0x01 to encodePong(packet)
        else -> throw IllegalArgumentException("Unexpected packet type: ${packet::class.simpleName}")
    }

    private fun encodeStatusResponse(packet: Packet.Status.StatusResponse): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeMCString(packet.json)
        return buf.toByteArray()
    }

    private fun encodePong(packet: Packet.Status.PongResponse): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeLong(packet.payload)
        return buf.toByteArray()
    }
}
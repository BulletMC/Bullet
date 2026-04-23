package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.OutboundPacket
import com.maddoxh.bullet.network.packet.impl.out.status.PongResponse
import com.maddoxh.bullet.network.packet.impl.out.status.StatusResponse
import java.io.ByteArrayOutputStream

object PacketWriter {
    fun serialize(packet: OutboundPacket): Pair<Int, ByteArray> = when(packet) {
        is StatusResponse -> 0x00 to encodeStatusResponse(packet)
        is PongResponse   -> 0x01 to encodePong(packet)
        else -> throw IllegalArgumentException("Unknown packet: $packet")
    }

    private fun encodeStatusResponse(packet: StatusResponse): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeMCString(packet.json)
        return buf.toByteArray()
    }

    private fun encodePong(packet: PongResponse): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeLong(packet.payload)
        return buf.toByteArray()
    }
}

package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.OutboundPacket
import com.maddoxh.bullet.network.packet.impl.out.config.ConfigDisconnect
import com.maddoxh.bullet.network.packet.impl.out.login.LoginDisconnect
import com.maddoxh.bullet.network.packet.impl.out.login.LoginSuccess
import com.maddoxh.bullet.network.packet.impl.out.play.PlayDisconnect
import com.maddoxh.bullet.network.packet.impl.out.status.PongResponse
import com.maddoxh.bullet.network.packet.impl.out.status.StatusResponse
import java.io.ByteArrayOutputStream

object PacketWriter {
    fun serialize(packet: OutboundPacket): Pair<Int, ByteArray> = when(packet) {
        is StatusResponse   -> 0x00 to encodeStatusResponse(packet)
        is PongResponse     -> 0x01 to encodePong(packet)
        is LoginSuccess     -> 0x02 to encodeLoginSuccess(packet)
        is LoginDisconnect  -> 0x00 to encodeDisconnectReason(packet.reason)
        is ConfigDisconnect -> 0x02 to encodeDisconnectReason(packet.reason)
        is PlayDisconnect   -> 0x1B to encodeDisconnectReason(packet.reason)

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

    private fun encodeLoginSuccess(packet: LoginSuccess): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)

        out.writeLong(packet.uuid.mostSignificantBits)
        out.writeLong(packet.uuid.leastSignificantBits)
        out.writeMCString(packet.username)
        out.writeVarInt(0)
        return buf.toByteArray()
    }

    private fun encodeDisconnectReason(reason: String): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeMCString(reason)
        return buf.toByteArray()
    }
}

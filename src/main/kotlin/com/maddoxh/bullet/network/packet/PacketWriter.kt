package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.OutboundPacket
import com.maddoxh.bullet.network.packet.impl.out.config.ClientboundKnownPacks
import com.maddoxh.bullet.network.packet.impl.out.config.ClientboundPluginMessage
import com.maddoxh.bullet.network.packet.impl.out.config.ConfigDisconnect
import com.maddoxh.bullet.network.packet.impl.out.config.FeatureFlags
import com.maddoxh.bullet.network.packet.impl.out.config.FinishConfiguration
import com.maddoxh.bullet.network.packet.impl.out.config.RegistryData
import com.maddoxh.bullet.network.packet.impl.out.config.UpdateTags
import com.maddoxh.bullet.network.packet.impl.out.login.LoginDisconnect
import com.maddoxh.bullet.network.packet.impl.out.login.LoginSuccess
import com.maddoxh.bullet.network.packet.impl.out.play.PlayDisconnect
import com.maddoxh.bullet.network.packet.impl.out.status.PongResponse
import com.maddoxh.bullet.network.packet.impl.out.status.StatusResponse
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.BinaryTagIO
import java.io.ByteArrayOutputStream

object PacketWriter {
    fun serialize(packet: OutboundPacket): Pair<Int, ByteArray> = when(packet) {
        is StatusResponse           -> 0x00 to encodeStatusResponse(packet)
        is PongResponse             -> 0x01 to encodePong(packet)
        is LoginSuccess             -> 0x02 to encodeLoginSuccess(packet)
        is LoginDisconnect          -> 0x00 to encodeDisconnectReason(packet.reason)
        is ConfigDisconnect         -> 0x02 to encodeDisconnectReason(packet.reason)
        is PlayDisconnect           -> 0x1B to encodeDisconnectReason(packet.reason)
        is ClientboundPluginMessage -> 0x01 to encodePluginMessage(packet)
        is FeatureFlags             -> 0x0C to encodeFeatureFlags()
        is ClientboundKnownPacks    -> 0x0E to encodeKnownPacks()
        is RegistryData             -> 0x07 to encodeRegistryData(packet)
        is UpdateTags               -> 0x0D to encodeUpdateTags()
        is FinishConfiguration      -> 0x03 to ByteArray(0)

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

    private fun encodePluginMessage(packet: ClientboundPluginMessage): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)

        out.writeMCString(packet.channel)
        out.write(packet.data)
        return buf.toByteArray()
    }

    private fun encodeFeatureFlags(): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)

        out.writeVarInt(1)
        out.writeMCString("minecraft:vanilla")
        return buf.toByteArray()
    }

    private fun encodeUpdateTags(): ByteArray {
        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).writeVarInt(0)
        return buf.toByteArray()
    }

    private fun encodeKnownPacks(): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)

        out.writeVarInt(1)
        out.writeMCString("minecraft")
        out.writeMCString("core")
        out.writeMCString("1.21.4")
        return buf.toByteArray()
    }

    private fun encodeRegistryData(packet: RegistryData): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)

        out.writeMCString(packet.registryID)
        out.writeVarInt(packet.entries.size)
        for(entry in packet.entries) {
            out.writeMCString(entry.id)
            if(entry.data != null) {
                out.writeBoolean(true)
                val nbtBuf = ByteArrayOutputStream()
                BinaryTagIO.writer().write(entry.data, nbtBuf)
                val nbtBytes = nbtBuf.toByteArray()
                out.writeByte(nbtBytes[0].toInt())
                out.write(nbtBytes, 3, nbtBytes.size - 3)
            } else {
                out.writeBoolean(false)
            }
        }

        return buf.toByteArray()
    }
}

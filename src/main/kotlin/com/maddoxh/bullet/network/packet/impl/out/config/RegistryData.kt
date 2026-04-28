package com.maddoxh.bullet.network.packet.impl.out.config

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.ConfigOutboundPacket
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import java.io.ByteArrayOutputStream

data class RegistryEntry(
    val id: String,
    val data: CompoundBinaryTag?
)

data class RegistryData( // 0x07 S->C
    val registryID: String,
    val entries: List<RegistryEntry>
) : ConfigOutboundPacket {
    override val packetId = 0x07
    override fun encode() = MinecraftOutputStream.build {
        writeMCString(registryID)
        writeVarInt(entries.size)
        for(entry in entries) {
            writeMCString(entry.id)
            if(entry.data != null) {
                writeBoolean(true)
                val nbtBuf = ByteArrayOutputStream()
                BinaryTagIO.writer().write(entry.data, nbtBuf)
                val nbtBytes = nbtBuf.toByteArray()
                writeByte(nbtBytes[0].toInt())
                write(nbtBytes, 3, nbtBytes.size - 3)
            } else {
                writeBoolean(false)
            }
        }
    }
}

package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import java.io.ByteArrayOutputStream

data class ChunkDataAndUpdateLight(
    val chunkX: Int,
    val chunkZ: Int,
) : PlayOutboundPacket {
    override val packetId = 0x27
    private val SECTION_COUNT = 24

    override fun encode(): ByteArray {
        val chunkData = buildChunkData()
        return MinecraftOutputStream.build {
            writeInt(chunkX)
            writeInt(chunkZ)

            val heightmaps = CompoundBinaryTag.builder()
                .put("MOTION_BLOCKING", IntArrayBinaryTag.intArrayBinaryTag(*IntArray(37) { 0 }))
                .put("WORLD_SURFACE", IntArrayBinaryTag.intArrayBinaryTag(*IntArray(37) { 0 }))
                .build()

            val nbtBuf = ByteArrayOutputStream()
            BinaryTagIO.writer().write(heightmaps, nbtBuf)
            val nbtBytes = nbtBuf.toByteArray()
            write(nbtBytes, 3, nbtBytes.size - 3)

            writeVarInt(chunkData.size)
            write(chunkData)

            writeVarInt(0)
            writeLightData()
        }
    }

    private fun buildChunkData(): ByteArray {
        val buf = ByteArrayOutputStream()
        val out = MinecraftOutputStream(buf)
        repeat(SECTION_COUNT) {
            out.writeShort(0)
            out.writeBlockStates()
            out.writeBiomes()
        }

        return buf.toByteArray()
    }

    private fun MinecraftOutputStream.writeBlockStates() {
        writeByte(0)
        writeVarInt(0)
        writeVarInt(0)
    }

    private fun MinecraftOutputStream.writeBiomes() {
        writeByte(0)
        writeVarInt(0) // plains
        writeVarInt(0)
    }

    private fun MinecraftOutputStream.writeLightData() {
        val sectionCount = SECTION_COUNT + 2

        val skyLightMask = buildBitSet(sectionCount) { true }
        writeVarInt(skyLightMask.size)
        skyLightMask.forEach { writeLong(it) }

        val blockLightMask = buildBitSet(sectionCount) { false }
        writeVarInt(blockLightMask.size)
        blockLightMask.forEach { writeLong(it) }

        writeVarInt(blockLightMask.size)
        blockLightMask.forEach { writeLong(it) }

        writeVarInt(blockLightMask.size)
        blockLightMask.forEach { writeLong(it) }

        val fullLight = ByteArray(2048) { 0xFF.toByte() }
        writeVarInt(sectionCount)
        repeat(sectionCount) {
            writeVarInt(fullLight.size)
            write(fullLight)
        }

        writeVarInt(0)
    }

    private fun buildBitSet(size: Int, predicate: (Int) -> Boolean): List<Long> {
        val longs = mutableListOf<Long>()
        var current = 0L
        for(i in 0 until size) {
            if(predicate(i)) current = current or (1L shl (i % 64))
            if((i + 1) % 64 == 0) { longs.add(current); current = 0L }
        }

        if(size % 64 != 0) longs.add(current)
        return longs
    }
}

package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.ByteArrayOutputStream

/**
 * A chunk sent by the server to the client
 * Right now it generates a 16x1x16 chunk with stone on the bottom layer and air on the rest
 *
 * @param x The chunk x coordinate.
 * @param z The chunk z coordinate.
 */
class ServerChunkPacket(
    x: Int,
    z: Int
) : Packet(0x20) {
    init {
        // Chunk coordinates
        wrapper.writeInt(x)
        wrapper.writeInt(z)

        wrapper.writeBoolean(true) // Full chunk flag
        wrapper.writeVarInt(1)  // Primary bit mask

        val tag = CompoundTag()
        tag.putLongArray("MOTION_BLOCKING", createHeightmapData())
        tag.putLongArray("WORLD_SURFACE", createHeightmapData())

        val nbt = Nbt()
        nbt.toStream(tag, wrapper)

        // Biomes
        wrapper.writeVarInt(1024)
        repeat(1024) {
            wrapper.writeVarInt(1)
        }

        // Chunk section data.
        val sectionData = createStoneChunkSection()
        wrapper.writeVarInt(sectionData.size)
        wrapper.write(sectionData)

        // com.aznos.world.blocks.com.aznos.world.blocks.Block entities count
        wrapper.writeVarInt(0)
    }

    /**
     * Creates a heightmap as a long array for 256 columns.
    */
    private fun createHeightmapData(): LongArray {
        val result = LongArray(37) { 0L }
        for(i in 0 until 256) {
            val longIndex = i / 7
            val offset = (i % 7) * 9
            result[longIndex] = result[longIndex] or (2L shl offset)
        }
        return result
    }

    private fun createStoneChunkSection(): ByteArray {
        val bitsPerBlock = 4
        val totalBlocks = 16 * 16 * 16

        val blockStates = IntArray(totalBlocks) { i ->
            val y = i / (16 * 16)
            if (y == 0) 1 else 0
        }

        val blockCount = 16 * 16

        val longArrayLength = (totalBlocks * bitsPerBlock + 63) / 64
        val packed = LongArray(longArrayLength) { 0L }
        for(i in 0 until totalBlocks) {
            val value = blockStates[i].toLong() and ((1L shl bitsPerBlock) - 1)
            val bitIndex = i * bitsPerBlock
            val longIndex = bitIndex / 64
            val bitOffset = bitIndex % 64

            if(bitOffset <= 64 - bitsPerBlock) {
                packed[longIndex] = packed[longIndex] or (value shl bitOffset)
            } else {
                val bitsInFirst = 64 - bitOffset
                packed[longIndex] = packed[longIndex] or (value shl bitOffset)
                packed[longIndex + 1] = packed[longIndex + 1] or (value shr bitsInFirst)
            }
        }

        val baos = ByteArrayOutputStream()
        baos.write((blockCount shr 8) and 0xFF)
        baos.write(blockCount and 0xFF)
        baos.write(bitsPerBlock)
        baos.writeVarInt(2)
        baos.writeVarInt(0)
        baos.writeVarInt(9)
        baos.writeVarInt(packed.size)

        for(l in packed) {
            baos.write((l shr 56 and 0xFF).toInt())
            baos.write((l shr 48 and 0xFF).toInt())
            baos.write((l shr 40 and 0xFF).toInt())
            baos.write((l shr 32 and 0xFF).toInt())
            baos.write((l shr 24 and 0xFF).toInt())
            baos.write((l shr 16 and 0xFF).toInt())
            baos.write((l shr 8 and 0xFF).toInt())
            baos.write((l and 0xFF).toInt())
        }

        baos.write(ByteArray(2048) { 0 })
        baos.write(ByteArray(2048) { 0xFF.toByte() })
        return baos.toByteArray()
    }
}
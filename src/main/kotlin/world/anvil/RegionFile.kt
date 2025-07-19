package com.aznos.world.anvil

import com.aznos.world.data.anvil.AnvilChunk
import net.querz.nbt.tag.CompoundTag
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream

class RegionFile(private val file: File) : Closeable {
    private val raf: RandomAccessFile

    private val locations = IntArray(1024)
    private val timestamps = IntArray(1024)

    init {
        file.parentFile.mkdirs()
        raf = RandomAccessFile(file, "rw")
        if(raf.length() < 8192L) {
            raf.setLength((8192L))
        }

        raf.seek(0)
        for(i in 0 until 1024) locations[i] = raf.readInt()
        for(i in 0 until 1024) timestamps[i] = raf.readInt()
    }

    private fun index(cx: Int, cz: Int): Int {
        val localX = (cx and 31)
        val localZ = (cz and 31)
        return localX + localZ * 32
    }

    fun readChunkNBT(cx: Int, cz: Int): CompoundTag? {
        val idx = index(cx, cz)
        val loc = locations[idx]
        if(loc == 0) return null
        val offset = (loc ushr 8) * 4096L
        val sectors = loc and 0xFF
        raf.seek(offset)
        val length = raf.readInt()
        val compression = raf.readUnsignedByte()
        val data = ByteArray(length - 1)
        raf.readFully(data)
        val input: InputStream = when(compression) {
            1 -> GZIPInputStream(ByteArrayInputStream(data))
            2 -> InflaterInputStream(ByteArrayInputStream(data))
            else -> return null
        }

        //return nbt data
        return null
    }

    fun writeChunk(chunk: AnvilChunk) {
        val idx = index(chunk.cz, chunk.cx)
        val nbtRoot = buildChunkNBT(chunk)
        //write nbt data
    }

    private fun buildChunkNBT(chunk: AnvilChunk): CompoundTag? {
        //build nbt data
        return null
    }

    override fun close() = raf.close()
}
package com.aznos.world.anvil

import com.aznos.world.data.anvil.AnvilChunk
import net.querz.nbt.io.NBTInputStream
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream
import javax.xml.namespace.QName

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
        if(length <= 0 || length > sectors * 4096) return null
        val compression = raf.readUnsignedByte()
        val data = ByteArray(length - 1)
        raf.readFully(data)
        val input: InputStream = when(compression) {
            1 -> GZIPInputStream(ByteArrayInputStream(data))
            2 -> InflaterInputStream(ByteArrayInputStream(data))
            else -> return null
        }

        NBTInputStream(input).use { nbtIn ->
            val named: NamedTag = nbtIn.readTag(512)
            return named.tag as CompoundTag
        }
    }

    fun writeChunk(chunk: AnvilChunk) {
        val idx = index(chunk.cx, chunk.cz)
        val nbtRoot = buildChunkNBT(chunk)
        val baos = ByteArrayOutputStream()
        val deflated = DeflaterOutputStream(baos)

        NBTOutputStream(deflated).use { out ->
            out.writeTag(NamedTag("", nbtRoot), 512)
        }

        deflated.close()
        val compressed = baos.toByteArray()
        val length = compressed.size + 1
        val neededSectors = ((length + 4) + 4095) / 4096
        val fileSectors = (raf.length() / 4096).toInt()
        val offsetSectors = fileSectors
        raf.seek(raf.length())
        val full = ByteBuffer.allocate(neededSectors * 4096)
        full.putInt(length)
        full.put(2)
        full.put(compressed)

        while(full.position() % 4096 != 0) full.put(0)
        raf.write(full.array())

        locations[idx] = (offsetSectors shl 8) or neededSectors
        timestamps[idx] = (System.currentTimeMillis() / 1000).toInt()

        raf.seek(0)
        for(i in 0 until 1024) raf.writeInt(locations[i])
        for(i in 0 until 1024) raf.writeInt(timestamps[i])
    }

    private fun buildChunkNBT(chunk: AnvilChunk): CompoundTag? {
        val root = CompoundTag()
        val level = CompoundTag()
        level.putInt("xPos", chunk.cx)
        level.putInt("zPos", chunk.cz)
        level.putString("Status", "full")
        level.putLong("InhabitedTime", 0)
        level.putLong("LastUpdate", System.currentTimeMillis())

        val sectionsList = ListTag(CompoundTag::class.java)
        for((_, sec) in chunk.sections) {
            val s = CompoundTag()
            s.putByte("Y", sec.y.toByte())

            val paletteList = ListTag(CompoundTag::class.java)
            sec.palette.forEach { pe ->
                val peTag = CompoundTag()
                peTag.putString("Name", pe.name)
                pe.props?.let { mp ->
                    if(mp.isNotEmpty()) {
                        val propTag = CompoundTag()
                        for((k, v) in mp) propTag.putString(k, v)
                        peTag.put("Properties", propTag)
                    }
                }
                paletteList.add(peTag)
            }

            s.put("Palette", paletteList)
            s.putLongArray("BlockStates", sec.blockStates)
            sectionsList.add(s)
        }

        level.put("Sections", sectionsList)
        root.put("Level", level)
        return root
    }

    override fun close() = raf.close()
}
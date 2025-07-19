package com.aznos.packets.play.out

import com.aznos.Bullet
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.serialization.NBTJson
import com.aznos.world.World
import com.aznos.world.blocks.Block
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag
import java.io.ByteArrayOutputStream
import kotlin.math.ceil

/**
 * A chunk sent by the server to the client
 * Right now it generates a 16x1x16 chunk with stone on the bottom layer and air on the rest
 *
 * @param x The chunk x coordinate.
 * @param z The chunk z coordinate.
 */
class ServerChunkPacket(
    private val world: World,
    private val cx: Int,
    private val cz: Int,
    private val fullChunk: Boolean = false
) : Packet(0x20) {
    init {
        wrapper.writeInt(cx)
        wrapper.writeInt(cz)

        wrapper.writeBoolean(fullChunk)
        val sectionBuffers = ArrayList<ByteArray>(16)
        var primaryBitMask = 0

        for(sectionY in 0 until 16) {
            val section = buildSection(sectionY) ?: continue
            primaryBitMask = primaryBitMask or (1 shl sectionY)
            sectionBuffers += section
        }

        wrapper.writeVarInt(primaryBitMask)
        wrapper.write(serializeHeightmapNBT())

        if(fullChunk) {
            wrapper.writeVarInt(1024)
            repeat(1024) {
                wrapper.writeVarInt(1)
            }
        }

        val concatenated = ByteArrayOutputStream()
        sectionBuffers.forEach { concatenated.write(it) }
        val sectionData = concatenated.toByteArray()
        wrapper.writeVarInt(sectionData.size)
        wrapper.write(sectionData)

        wrapper.writeVarInt(0) // Block entities count
        // After building all sections in ServerChunkPacket.write()
        Bullet.logger.info("Chunk ($cx,$cz) mask=${primaryBitMask.toString(16)} sections=${sectionBuffers.size} blocks=${sectionData.size / 4096} fullChunk=$fullChunk")
    }

    private fun buildSection(sectionY: Int): ByteArray? {
        val baseY = sectionY * 16
        var nonAir = 0
        val tmpStates = IntArray(4096)
        var i = 0

        for(y in 0 until 16) {
            val wy = baseY + y
            for(z in 0 until 16) {
                val wz = (cz shl 4) + z
                for(x in 0 until 16) {
                    val wx = (cx shl 4) + x
                    val internal = world.getBlockId(wx, wy, wz)
                    val stateID = Block.defaultStateIdForInternal(internal)
                    if(internal != Block.AIR.id) nonAir++
                    tmpStates[i++] = stateID
                }
            }
        }

        if(sectionY == 0 && nonAir == 0) {
            Bullet.logger.warn("Section 0 empty at chunk $cx $cz – expected ground?")
        }

        if(nonAir == 0) return null
        val (bits, palette, remapped) = buildPalette(tmpStates)
        val dataArray = pack(remapped, bits)
        val baos = ByteArrayOutputStream()

        baos.write((nonAir ushr 8) and 0xFF)
        baos.write(nonAir and 0xFF)

        baos.write(bits)
        if(bits <= 8) {
            baos.writeVarInt(palette.size)
            palette.forEach { baos.writeVarInt(it) }
        }

        baos.writeVarInt(dataArray.size)
        for(l in dataArray) {
            baos.write(((l ushr 56) and 0xFF).toInt())
            baos.write(((l ushr 48) and 0xFF).toInt())
            baos.write(((l ushr 40) and 0xFF).toInt())
            baos.write(((l ushr 32) and 0xFF).toInt())
            baos.write(((l ushr 24) and 0xFF).toInt())
            baos.write(((l ushr 16) and 0xFF).toInt())
            baos.write(((l ushr 8) and 0xFF).toInt())
            baos.write((l and 0xFF).toInt())
        }

        return baos.toByteArray()
    }

    private fun buildPalette(states: IntArray): Triple<Int, IntArray, IntArray> {
        val map = HashMap<Int, Int>(states.size / 8)
        val palette = ArrayList<Int>(64)
        val remap = IntArray(4096)

        for(i in states.indices) {
            val id = states[i]
            val idx = map[id]
            if(idx == null) {
                val pIndex = palette.size
                palette += id
                map[id] = pIndex
                remap[i] = pIndex
            } else {
                remap[i] = idx
            }
        }

        val size = palette.size
        var bits = maxOf(4, ceilLog2(size))
        val direct = bits > 8
        if(direct) {
            bits = 14
            for (i in remap.indices) remap[i] = states[i]
            return Triple(bits, IntArray(0), remap)
        }

        return Triple(bits, palette.toIntArray(), remap)
    }

    private fun pack(values: IntArray, bits: Int): LongArray {
        val perLong = 64 / bits
        val longs = ceil(values.size / perLong.toDouble()).toInt()
        val arr = LongArray(longs)
        for(i in values.indices) {
            val v = values[i].toLong()
            val li = i / perLong
            val shift = (i % perLong) * bits
            arr[li] = arr[li] or ((v and ((1L shl bits) - 1)) shl shift)
        }

        return arr
    }

    private fun ceilLog2(v0: Int): Int {
        if(v0 <= 1) return 0
        var v = v0 - 1
        var c = 0
        while(v > 0) {
            v = v ushr 1
            c++
        }

        return c
    }

    private fun serializeHeightmapNBT(): ByteArray {
        val root = CompoundTag()
        val motion = LongArray(37)
        val surface = LongArray(37)

        for(i in 0 until 256) {
            val lx = i and 15
            val lz = (i ushr 4) and 15
            val wx = (cx shl 4) + lx
            val wz = (cz shl 4) + lz
            var top = 0
            for(y in 255 downTo 0) {
                if (world.getBlockId(wx, y, wz) != Block.AIR.id) {
                    top = y + 1
                    break
                }
            }

            val h = top.coerceIn(0, 511)
            val longIndex = i / 7
            val shift = (i % 7) * 9
            motion[longIndex] = motion[longIndex] or (h.toLong() shl shift)
            surface[longIndex] = surface[longIndex] or (h.toLong() shl shift)
        }

        root.putLongArray("MOTION_BLOCKING", motion)
        root.putLongArray("WORLD_SURFACE", surface)

        val baos = ByteArrayOutputStream()
        NBTOutputStream(baos).use { out ->
            out.writeTag(NamedTag("", root), 512)
        }

        return baos.toByteArray()
    }
}
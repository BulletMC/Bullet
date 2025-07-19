package com.aznos.packets.play.out

import com.aznos.Bullet
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.world.World
import com.aznos.world.anvil.PaletteIndex
import com.aznos.world.data.anvil.AnvilSection
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.VanillaBlockRegistry
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag
import java.io.ByteArrayOutputStream
import kotlin.math.max

class ServerChunkPacket(
    private val world: World,
    private val cx: Int,
    private val cz: Int,
    private val fullChunk: Boolean,
    private val sections: Map<Int, AnvilSection>,       // pass world chunk.sections
    private val biomesProvider: (() -> IntArray)? = null // returns 1024 biome ints if fullChunk
) : Packet(0x20) {

    companion object {
        private const val MAX_SECTION = 15
        private val BLOCK_LIGHT_BYTES = ByteArray(2048) { 0x00 }
        private val SKY_LIGHT_BYTES   = ByteArray(2048) { 0xFF.toByte() }
        private val AIR_STATE = Block.getStateID(Block.AIR)
    }

    init {
        wrapper.writeInt(cx)
        wrapper.writeInt(cz)
        wrapper.writeBoolean(fullChunk)

        val sectionBytesList = ArrayList<ByteArray>(16)
        var mask = 0
        val topHeights = IntArray(16 * 16) { -1 }

        for(sy in 0..MAX_SECTION) {
            val anvilSec = sections[sy] ?: continue
            val built = buildNetworkSection(anvilSec, sy, topHeights) ?: continue
            mask = mask or (1 shl sy)
            sectionBytesList += built
        }

        wrapper.writeVarInt(mask)
        wrapper.write(serializeHeightmap(topHeights))

        if(fullChunk) {
            val biomes = biomesProvider?.invoke() ?: IntArray(1024) { 1 }
            wrapper.writeVarInt(1024)
            for (b in biomes) wrapper.writeVarInt(b)
        }

        val combined = ByteArrayOutputStream()
        sectionBytesList.forEach { combined.write(it) }
        val sectionData = combined.toByteArray()

        wrapper.writeVarInt(sectionData.size)
        wrapper.write(sectionData)

        wrapper.writeVarInt(0) // block entity count
    }

    private fun buildNetworkSection(
        sec: AnvilSection,
        sectionY: Int,
        topHeights: IntArray
    ): ByteArray? {
        if(sec.palette.size == 1 && sec.palette[0].name == "minecraft:air") return null
        val paletteGlobal = IntArray(sec.palette.size) { i ->
            val pe = sec.palette[i]
            Block.idFor(pe.name, pe.props)
        }

        val indices = IntArray(4096)
        var nonAir = 0
        for(i in 0 until 4096) {
            val pi = PaletteIndex.getPaletteIndex(sec.blockStates, sec.bitsPerBlock, i)
            indices[i] = pi
            if(pi < paletteGlobal.size) {
                val stateId = paletteGlobal[pi]
                if(stateId != AIR_STATE) {
                    nonAir++
                    val localY = i / 256
                    val rem = i % 256
                    val localZ = rem / 16
                    val localX = rem % 16
                    val wy = sectionY * 16 + localY
                    val columnIndex = (localZ shl 4) or localX
                    if(wy > topHeights[columnIndex]) topHeights[columnIndex] = wy
                }
            }
        }

        if(nonAir == 0) return null
        val distinctStates = IntArray(paletteGlobal.size) { paletteGlobal[it] }
        val paletteSize = distinctStates.size
        val paletteBits = max(4, ceilLog2(paletteSize))
        val direct = paletteBits > 8

        val baos = ByteArrayOutputStream()
        baos.write((nonAir ushr 8) and 0xFF)
        baos.write(nonAir and 0xFF)

        if(!direct) {
            baos.write(paletteBits)
            baos.writeVarInt(paletteSize)
            for (gid in distinctStates) baos.writeVarInt(gid)

            val packed = packIndices(indices, paletteBits)
            baos.writeVarInt(packed.size)
            packed.forEach { l -> writeLongBE(baos, l) }
        } else {
            val bits = 14
            baos.write(bits)
            val globals = IntArray(4096) { paletteGlobal[indices[it]] }
            val packed = packIndices(globals, bits)
            baos.writeVarInt(packed.size)
            packed.forEach { l -> writeLongBE(baos, l) }
        }

        baos.write(BLOCK_LIGHT_BYTES)
        baos.write(SKY_LIGHT_BYTES)
        return baos.toByteArray()
    }

    private data class PaletteBuild(
        val bits: Int,
        val palette: IntArray,
        val remap: IntArray,
        val direct: Boolean
    )

    private fun buildLocalPalette(states: IntArray): PaletteBuild {
        val map = HashMap<Int, Int>(states.size / 8)
        val palette = ArrayList<Int>(64)
        val remap = IntArray(states.size)

        for (i in states.indices) {
            val s = states[i]
            val existing = map[s]
            if (existing == null) {
                val pi = palette.size
                map[s] = pi
                palette += s
                remap[i] = pi
            } else {
                remap[i] = existing
            }
        }

        val pSize = palette.size
        var bits = max(4, ceilLog2(pSize))
        val direct = bits > 8
        if (direct) {
            bits = 14
            for (i in remap.indices) remap[i] = states[i]
            return PaletteBuild(bits, IntArray(0), remap, true)
        }

        return PaletteBuild(bits, palette.toIntArray(), remap, false)
    }

    private fun ceilLog2(v0: Int): Int {
        if (v0 <= 1) return 0
        var v = v0 - 1
        var c = 0
        while (v > 0) { v = v ushr 1; c++ }
        return c
    }

    private fun packValues(values: IntArray, bits: Int): LongArray {
        val perLong = 64 / bits
        val longs = (values.size + perLong - 1) / perLong
        val arr = LongArray(longs)
        val mask = (1L shl bits) - 1
        for (i in values.indices) {
            val li = i / perLong
            val shift = (i % perLong) * bits
            arr[li] = arr[li] or ((values[i].toLong() and mask) shl shift)
        }
        return arr
    }

    private fun serializeHeightmap(topHeights: IntArray): ByteArray {
        val root = CompoundTag()
        val motion = LongArray(37)
        val surface = LongArray(37)
        for (col in 0 until 256) {
            val top = topHeights[col]
            val h = (top + 1).coerceAtLeast(0).coerceIn(0, 511)
            val li = col / 7
            val shift = (col % 7) * 9
            motion[li] = motion[li] or (h.toLong() shl shift)
            surface[li] = surface[li] or (h.toLong() shl shift)
        }
        root.putLongArray("MOTION_BLOCKING", motion)
        root.putLongArray("WORLD_SURFACE", surface)

        val baos = ByteArrayOutputStream()
        NBTOutputStream(baos).use { out -> out.writeTag(NamedTag("", root), 512) }
        return baos.toByteArray()
    }

    private fun packIndices(values: IntArray, bits: Int): LongArray {
        val perLong = 64 / bits
        val longs = (values.size + perLong - 1) / perLong
        val arr = LongArray(longs)
        val mask = (1L shl bits) - 1
        for(i in values.indices) {
            val li = i / perLong
            val shift = (i % perLong) * bits
            arr[li] = arr[li] or ((values[i].toLong() and mask) shl shift)
        }

        return arr
    }

    private fun writeLongBE(out: ByteArrayOutputStream, v: Long) {
        out.write(((v ushr 56) and 0xFF).toInt())
        out.write(((v ushr 48) and 0xFF).toInt())
        out.write(((v ushr 40) and 0xFF).toInt())
        out.write(((v ushr 32) and 0xFF).toInt())
        out.write(((v ushr 24) and 0xFF).toInt())
        out.write(((v ushr 16) and 0xFF).toInt())
        out.write(((v ushr 8) and 0xFF).toInt())
        out.write((v and 0xFF).toInt())
    }
}
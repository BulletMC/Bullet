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

        // Iterate lowest -> highest (0..15)
        for (sy in 0..MAX_SECTION) {
            val anvilSec = sections[sy] ?: continue
            val built = buildNetworkSection(anvilSec, sy, topHeights) ?: continue
            mask = mask or (1 shl sy)
            sectionBytesList += built
        }

        wrapper.writeVarInt(mask)
        wrapper.write(serializeHeightmap(topHeights))

        if (fullChunk) {
            val biomes = biomesProvider?.invoke() ?: IntArray(1024) { 1 } // plains
            wrapper.writeVarInt(1024)
            for (b in biomes) wrapper.writeVarInt(b)
        }

        val combined = ByteArrayOutputStream()
        sectionBytesList.forEach { combined.write(it) }
        val sectionData = combined.toByteArray()

        wrapper.writeVarInt(sectionData.size)
        wrapper.write(sectionData)

        wrapper.writeVarInt(0) // block entity count

        Bullet.logger.info(
            "Chunk ($cx,$cz) full=$fullChunk mask=0x${mask.toString(16)} " +
                    "sec=${sectionBytesList.size} bytes=${sectionData.size}"
        )
    }

    private fun buildNetworkSection(
        sec: AnvilSection,
        sectionY: Int,
        topHeights: IntArray
    ): ByteArray? {
        val bitsStored = sec.bitsPerBlock
        val blockStates = sec.blockStates
        val paletteNames = sec.palette

        // Fast skip if palette only air
        if (paletteNames.size == 1 && paletteNames[0] == "minecraft:air") return null

        // Decode all 4096 entries → global state IDs
        val states = IntArray(4096)
        var nonAir = 0
        var idx = 0
        for (y in 0 until 16) {
            val wy = sectionY * 16 + y
            for (z in 0 until 16) {
                val colBase = z shl 4
                for (x in 0 until 16) {
                    val localIndex = idx
                    val pi = PaletteIndex.getPaletteIndex(blockStates, bitsStored, localIndex)
                    val vanillaName = paletteNames.getOrNull(pi) ?: "minecraft:air"
                    val internal = VanillaBlockRegistry.toInternal(vanillaName)
                    val stateId = if (internal == Block.AIR.id) AIR_STATE
                    else Block.defaultStateIdForInternal(internal)
                    states[localIndex] = stateId
                    if (internal != Block.AIR.id) {
                        nonAir++
                        val columnIndex = colBase + x
                        val prevTop = topHeights[columnIndex]
                        if (wy > prevTop) topHeights[columnIndex] = wy
                    }
                    idx++
                }
            }
        }

        if (nonAir == 0) return null

        val (bits, palette, remap, direct) = buildLocalPalette(states)
        val dataArray = packValues(remap, bits)

        val baos = ByteArrayOutputStream()
        // blockCount
        baos.write((nonAir ushr 8) and 0xFF)
        baos.write(nonAir and 0xFF)
        // bits per block
        baos.write(bits)

        if (!direct) {
            baos.writeVarInt(palette.size)
            palette.forEach { baos.writeVarInt(it) }
        }

        baos.writeVarInt(dataArray.size)
        dataArray.forEach { l ->
            for (shift in 56 downTo 0 step 8) {
                baos.write(((l ushr shift) and 0xFF).toInt())
            }
        }

        // Embedded light arrays
        baos.write(BLOCK_LIGHT_BYTES)
        baos.write(SKY_LIGHT_BYTES)

        Bullet.logger.debug(
            "SecY=$sectionY nonAir=$nonAir bits=$bits palette=${palette.size} longs=${dataArray.size} direct=$direct"
        )
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
            for (i in remap.indices) remap[i] = states[i] // direct = global IDs
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
}
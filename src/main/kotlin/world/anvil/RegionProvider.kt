package com.aznos.world.anvil

import com.aznos.world.data.anvil.AnvilChunk
import com.aznos.world.data.anvil.AnvilSection
import com.aznos.world.data.anvil.PaletteEntry
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.ListTag
import net.querz.nbt.tag.Tag
import org.apache.logging.log4j.core.util.Integers
import java.io.File
import java.lang.Math.floorDiv

class RegionProvider(private val worldRoot: File) {
    private val regionDir = File(worldRoot, "region")
    private val cache = mutableMapOf<Long, AnvilChunk>()
    private val dirty = mutableSetOf<AnvilChunk>()

    fun key(cx: Int, cz: Int) = (cx.toLong() shl 32) xor (cz.toLong() and 0xffffffffL)
    fun getChunk(cx: Int, cz: Int): AnvilChunk? {
        return cache[key(cx, cz)] ?: loadChunk(cx, cz)
    }

    fun getOrLoadChunk(cx: Int, cz: Int): AnvilChunk = getChunk(cx, cz) ?: createEmptyChunk(cx, cz)
    private fun createEmptyChunk(cx: Int, cz: Int): AnvilChunk {
        val chunk = AnvilChunk(cx, cz, mutableMapOf())
        cache[key(cx, cz)] = chunk
        chunk.dirty = true
        dirty += chunk
        return chunk
    }

    fun createEmptySection(y: Int): AnvilSection {
        val palette = mutableListOf(PaletteEntry("minecraft:air"))
        val bits = 4
        val longs = LongArray((4096 * bits + 63) / 64)
        return AnvilSection(y, palette, bits, longs)
    }


    private fun loadChunk(cx: Int, cz: Int): AnvilChunk? {
        val regionFile = regionFile(cx, cz)
        if(!regionFile.exists()) return null

        RegionFile(regionFile).use { rf ->
            val root = rf.readChunkNBT(cx, cz) ?: return null
            val level = root.comp("Level") ?: return null
            val sectionsList = level.list("Sections") ?: return null
            val sections = mutableMapOf<Int, AnvilSection>()

            for (i in 0 until sectionsList.size()) {
                val secTag = sectionsList[i] as? CompoundTag ?: continue
                val y = secTag.getByte("Y").toInt()
                if(y !in 0..15) continue

                val paletteListTag = secTag.list("Palette") ?: continue
                val palette = ArrayList<PaletteEntry>(paletteListTag.size())
                for(p in 0 until paletteListTag.size()) {
                    val entryTag = paletteListTag[p] as? CompoundTag ?: continue
                    val name = entryTag.getString("Name")
                    val propTag = entryTag.get("Properties") as? CompoundTag
                    val props: Map<String, String>? = propTag?.keySet()?.associateWith { k -> propTag.getString(k) }
                    palette += PaletteEntry(name, props)
                }
                if(palette.isEmpty()) palette += PaletteEntry("minecraft:air")
                val blockStatesArray: LongArray =
                    secTag.longArray("BlockStates") ?: LongArray((4096 * 4 + 63) / 64)

                val bits = bitsForPaletteSize(palette.size)
                sections[y] = AnvilSection(y, palette.toMutableList(), bits, blockStatesArray)
            }

            val chunk = AnvilChunk(cx, cz, sections)
            cache[key(cx, cz)] = chunk
            return chunk
        }
    }

    private fun bitsForPaletteSize(paletteSize: Int): Int {
        return if(paletteSize <= 1) 4
        else maxOf(4, 32 - Integer.numberOfLeadingZeros(paletteSize - 1))
    }

    private fun regionFile(cx: Int, cz: Int): File {
        val rx = floorDiv(cx, 32)
        val rz = floorDiv(cz, 32)
        return File(regionDir, "r.$rx.$rz.mca")
    }

    fun repackSection(section: AnvilSection, oldBits: Int) {
        val newBits = section.bitsPerBlock
        if (oldBits == newBits) return

        val oldArr = section.blockStates
        val newArr = LongArray((4096 * newBits + 63) / 64)
        for (i in 0 until 4096) {
            val pi = PaletteIndex.getPaletteIndex(oldArr, oldBits, i)
            PaletteIndex.setPaletteIndex(newArr, newBits, i, pi)
        }

        section.blockStates = newArr
    }

    fun flushDirty() {
        if (dirty.isEmpty()) return
        val byRegion = dirty.groupBy { Pair(floorDiv(it.cx, 32), floorDiv(it.cz, 32)) }
        for ((regPair, chunks) in byRegion) {
            val (rx, rz) = regPair
            val rf = RegionFile(regionFile(rx * 32, rz * 32))
            for (chunk in chunks) {
                rf.writeChunk(chunk)
                chunk.dirty = false
            }

            rf.close()
        }

        dirty.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun CompoundTag.comp(name: String): CompoundTag? =
        this.get(name) as? CompoundTag

    @Suppress("UNCHECKED_CAST")
    private fun CompoundTag.list(name: String): ListTag<Tag<*>>? =
        this.get(name) as? ListTag<Tag<*>>

    @Suppress("UNCHECKED_CAST")
    private fun CompoundTag.longArray(name: String): LongArray? =
        (this.get(name) as? net.querz.nbt.tag.LongArrayTag)?.value
}
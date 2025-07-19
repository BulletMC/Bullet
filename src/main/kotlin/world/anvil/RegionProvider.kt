package com.aznos.world.anvil

import com.aznos.world.data.anvil.AnvilChunk
import com.aznos.world.data.anvil.AnvilSection
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
        val palette = mutableListOf("minecraft:air")
        val bits = 4
        val longs = LongArray((4096 * bits + 63) / 64)
        return AnvilSection(y, palette, bits, longs)
    }

    private fun loadChunk(cx: Int, cz: Int): AnvilChunk? {
        val regionFile = regionFile(cx, cz)
        if(!regionFile.exists()) return null
        val rf = RegionFile(regionFile)
        //nbt data later
        return null
    }

    private fun regionFile(cx: Int, cz: Int): File {
        val rx = floorDiv(cx, 32)
        val rz = floorDiv(cz, 32)
        return File(regionDir, "r.$rx.$rz.mca")
    }

    fun repackSection(section: AnvilSection) {
        val newBits = section.bitsPerBlock
        val newArr = LongArray((4096 * newBits + 63) / 64)
        for(i in 0 until 4096) {
            val oldBits = newBits
            val pi = PaletteIndex.getPaletteIndex(section.blockStates, oldBits, i)
            PaletteIndex.setPaletteIndex(newArr, newBits, i, pi)
        }

        section.blockStates = newArr
    }

    fun flushDirty() {
        if(dirty.isEmpty()) return
        val byRegion = dirty.groupBy { Pair(floorDiv(it.cx,32), floorDiv(it.cz,32)) }
        for((regPair, chunks) in byRegion) {
            val (rx, rz) = regPair
            val rf = RegionFile(regionFile(rx * 32, rz * 32))
            for(chunk in chunks) {
                rf.writeChunk(chunk)
                chunk.dirty = false
            }

            rf.close()
        }

        dirty.clear()
    }
}
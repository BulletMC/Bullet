package com.aznos.world.anvil

import com.aznos.Bullet
import com.aznos.storage.disk.AnvilWorldStorage
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockAccess
import com.aznos.world.blocks.VanillaBlockRegistry
import com.aznos.world.data.anvil.AnvilChunk
import com.aznos.world.data.anvil.AnvilSection
import kotlin.collections.get

class AnvilBlockAccess(private val storage: AnvilWorldStorage) : BlockAccess {
    private val provider = RegionProvider(storage.root)

    override fun getBlockID(x: Int, y: Int, z: Int): Int {
        if(y !in 0..255) return 0
        val chunk = provider.getChunk(x shr 4, z shr 4)

        if(chunk == null && y == 0 && x == 0 && z == 0) {
            Bullet.logger.info("Fallback grass at global (0,0,0)")
        }

        if(chunk == null) {
            return if(y == 0) Block.GRASS_BLOCK.id else Block.AIR.id
        }

        val section = chunk.sections[y shr 4] ?: return if(y == 0) Block.GRASS_BLOCK.id else Block.AIR.id
        val idx = ((y and 15) * 16 + (z and 15)) * 16 + (x and 15)
        val pi = PaletteIndex.getPaletteIndex(section.blockStates, section.bitsPerBlock, idx)
        val vanilla = section.palette.getOrNull(pi) ?: "minecraft:air"
        return VanillaBlockRegistry.toInternal(vanilla)
    }

    override fun setBlockID(x: Int, y: Int, z: Int, blockID: Int) {
        if(y !in 0..255) return
        val chunk = provider.getOrLoadChunk(x shr 4, z shr 4)
        val sectionY = y shr 4
        val section = chunk.sections.getOrPut(sectionY) { provider.createEmptySection(sectionY) }
        val vanillaName = VanillaBlockRegistry.toVanilla(blockID)

        var paletteIndex = section.palette.indexOf(vanillaName)
        if(paletteIndex == -1) {
            section.palette.add(vanillaName)
            val neededBits = maxOf(4, (32 - Integer.numberOfLeadingZeros(section.palette.size - 1)))
            if(neededBits != section.bitsPerBlock) {
                val oldBits = section.bitsPerBlock
                section.bitsPerBlock = neededBits
                provider.repackSection(section, oldBits)
            }

            paletteIndex = section.palette.lastIndex
        }

        val idx = ((y and 15) * 16 + (z and 15)) * 16 + (x and 15)
        PaletteIndex.setPaletteIndex(section.blockStates, section.bitsPerBlock, idx, paletteIndex)
        chunk.dirty = true
    }

    fun flushDirty() = provider.flushDirty()

    fun getLoadedOrDiskChunk(cx: Int, cz: Int): AnvilChunk? = provider.getChunk(cx, cz)
    fun getOrCreateChunk(cx: Int, cz: Int): AnvilChunk = provider.getOrLoadChunk(cx, cz)
    fun getSections(cx: Int, cz: Int): Map<Int, AnvilSection> = provider.getChunk(cx, cz)?.sections ?: emptyMap()
}
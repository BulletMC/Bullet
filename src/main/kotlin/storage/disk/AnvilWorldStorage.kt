package com.aznos.storage.disk

import com.aznos.datatypes.BlockPositionType
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.WorldData
import java.io.File

class AnvilWorldStorage(private val name: String, val root: File) : AbstractWorldStorage {
    private val bulletDir = File(root, "bullet").apply { mkdirs() }

    override fun getName() = name
    override fun readWorldData(): WorldData? = null // TODO: Implement this
    override fun writeWorldData(data: WorldData): Boolean = true // TODO: Implement this

    override fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> {
        val f = File(bulletDir, "blocks_overlay.json")
        return DiskStorageUtil.readFileData(f, HashMap())
    }

    override fun writeBlockData(modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>): Boolean {
        val f = File(bulletDir, "blocks_overlay.json")
        return DiskStorageUtil.writeFileData(f, modifiedBlocks)
    }

    override fun writeEntities(entities: List<EntityData>): Boolean {
        //TODO: Implement this
        return true
    }

    override fun readEntities(): List<EntityData>? = null // TODO: Implement this
}
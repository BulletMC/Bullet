package com.aznos.storage.disk

import com.aznos.datatypes.BlockPositionType
import com.aznos.storage.disk.DiskStorageUtil.readFileData
import com.aznos.storage.disk.DiskStorageUtil.writeFileData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.WorldData
import java.io.File

class DiskWorldStorage(
    private val name: String,
    private val folder: File,
) : AbstractWorldStorage {

    companion object {
        private const val WORLD_DATA_FILE_NAME = "world.json"
        private const val WORLD_STORAGE_FILE_NAME = "blocks.json"
    }

    override fun getName(): String {
        return name
    }

    override fun readWorldData(): WorldData? {
        val file = File(folder, WORLD_DATA_FILE_NAME)
        return readFileData(file, null)
    }

    override fun writeWorldData(data: WorldData): Boolean {
        val file = File(folder, WORLD_DATA_FILE_NAME)
        return writeFileData(file, data)
    }

    override fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> {
        val file = File(folder, WORLD_STORAGE_FILE_NAME)
        return readFileData(file, HashMap())
    }

    override fun writeBlockData(
        modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>
    ): Boolean {
        val file = File(folder, WORLD_STORAGE_FILE_NAME)
        return writeFileData(file, modifiedBlocks)
    }

}
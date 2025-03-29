package com.aznos.storage.disk

import com.aznos.Bullet
import com.aznos.storage.disk.DiskStorageUtil.readFileData
import com.aznos.storage.disk.DiskStorageUtil.writeFileData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.WorldData
import java.io.File

class DiskWorldStorage(
    private val name: String,
    private val folder: File,
) : AbstractWorldStorage {

    companion object {
        private const val WORLD_FILE_NAME = "world.json"
    }

    override fun getName(): String {
        return name
    }

    override fun readWorldData(): WorldData? {
        val file = File(folder, WORLD_FILE_NAME)
        return readFileData(file, null)
    }

    override fun writeWorldData(data: WorldData): Boolean {
        assert(Bullet.shouldPersist) // We only want to save if persistence is enabled

        val file = File(folder, WORLD_FILE_NAME)
        return writeFileData(file, data)
    }

}
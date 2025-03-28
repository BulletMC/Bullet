package com.aznos.storage.disk

import com.aznos.Bullet
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.WorldData
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files

class DiskWorldStorage(
    private val name: String,
    private val folder: File,
) : AbstractWorldStorage {

    override fun getName(): String {
        return name
    }

    override fun writeWorldData(data: WorldData): Boolean {
        assert(Bullet.shouldPersist) // We only want to save if persistence is enabled

        folder.mkdirs()
        val file = File(folder, "world.json")
        if (!file.exists()) file.createNewFile()

        val json = Json.encodeToString(data)
        Files.write(file.toPath(), json.toByteArray())
        return true
    }

    override fun readWorldData(): WorldData? {
        val file = File(folder, "world.json")
        if (!file.exists()) return null

        val json = Files.readString(file.toPath())
        return Json.decodeFromString(json)
    }

}
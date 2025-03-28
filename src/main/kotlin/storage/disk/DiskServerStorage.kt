package com.aznos.storage.disk

import com.aznos.Bullet
import com.aznos.storage.AbstractServerStorage
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.PlayerData
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.util.*

class DiskServerStorage : AbstractServerStorage {

    companion object {
        private val STORAGE_ROOT = File(".")
        private val WORLDS_STORAGE_ROOT = File(STORAGE_ROOT, "worlds")
        private val PLAYER_STORAGE_ROOT = File(STORAGE_ROOT, "players")
    }

    override fun prepareWorldStorage(name: String): AbstractWorldStorage {
        return DiskWorldStorage(name, File(WORLDS_STORAGE_ROOT, name))
    }

    override fun readPlayerData(uuid: UUID): PlayerData? {
        val file = File(PLAYER_STORAGE_ROOT, "$uuid.json")
        if (!file.exists()) return null

        val json = Files.readString(file.toPath())
        return Json.decodeFromString(json)
    }

    override fun writePlayerData(data: PlayerData): Boolean {
        assert(Bullet.shouldPersist) // We only want to save if persistence is enabled

        PLAYER_STORAGE_ROOT.mkdirs()
        val file = File(PLAYER_STORAGE_ROOT, "${data.uuid}.json")
        if (!file.exists()) file.createNewFile()

        val json = Json.encodeToString(data)
        Files.write(file.toPath(), json.toByteArray())
        return true
    }

}
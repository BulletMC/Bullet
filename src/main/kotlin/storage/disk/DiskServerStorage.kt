package com.aznos.storage.disk

import com.aznos.Bullet
import com.aznos.entity.player.data.BanData
import com.aznos.storage.AbstractServerStorage
import com.aznos.storage.disk.DiskStorageUtil.readFileData
import com.aznos.storage.disk.DiskStorageUtil.writeFileData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.PlayerData
import java.io.File
import java.util.*

class DiskServerStorage : AbstractServerStorage {

    companion object {
        private val STORAGE_ROOT = File("./data")

        private val WORLDS_STORAGE_ROOT = File(STORAGE_ROOT, "worlds")
        private val PLAYER_STORAGE_ROOT = File(STORAGE_ROOT, "players")

        private const val BANNED_FILE_NAME = "banned_players.json"
    }

    override fun prepareWorldStorage(name: String): AbstractWorldStorage {
        return DiskWorldStorage(name, File(WORLDS_STORAGE_ROOT, name))
    }

    override fun readPlayerData(uuid: UUID): PlayerData? {
        val file = File(PLAYER_STORAGE_ROOT, "$uuid.json")
        return readFileData(file, null)
    }

    override fun writePlayerData(data: PlayerData): Boolean {
        assert(Bullet.shouldPersist) // We only want to save if persistence is enabled

        val file = File(PLAYER_STORAGE_ROOT, "${data.uuid}.json")
        return writeFileData(file, data)
    }

    override fun readBannedList(): Collection<BanData> {
        val file = File(STORAGE_ROOT, BANNED_FILE_NAME)
        return readFileData(file, Collections.emptyList())
    }

    override fun writeBannedList(banned: Collection<BanData>): Boolean {
        val file = File(STORAGE_ROOT, BANNED_FILE_NAME)
        return writeFileData(file, banned)
    }

}
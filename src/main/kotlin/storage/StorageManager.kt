package com.aznos.storage

import com.aznos.entity.player.Player
import com.aznos.entity.player.data.BanData
import com.aznos.storage.disk.AnvilWorldStorage
import com.aznos.world.World
import com.aznos.world.blocks.MapBlockAccess
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock

@Suppress("unused")
class StorageManager(val storage: AbstractServerStorage) {

    private val worldsLock = ReentrantReadWriteLock()
    private val worlds = HashMap<String, World>()

    private val banned = ConcurrentHashMap<UUID, BanData>()

    init {
        for (data in storage.readBannedList()) {
            banned[data.uuid] = data;
        }
    }

    /**
     * Get world with by name if exist
     *
     * @param name the world's name
     * @return the world with that name. null if do not exist
     */
    fun getWorld(name: String): World? {
        worldsLock.readLock().lock()
        val world = worlds[name]
        worldsLock.readLock().unlock()

        return world
    }

    /**
     * Get world with by name.
     * Will load and create a new world if already exist
     *
     * @param name the world's name
     * @return the world with that name
     */
    fun getOrLoadWorld(name: String): World {
        // We assume world present and should not lock write
        worldsLock.readLock().lock()
        var world = worlds[name]
        if (world != null) {
            worldsLock.readLock().unlock()
            return world
        }

        worldsLock.readLock().unlock()
        worldsLock.writeLock().lock()

        // As we just acquired the write lock, we check again the map in case there was a race condition
        world = worlds[name]
        if (world != null) {
            worldsLock.writeLock().unlock()
            return world
        }

        // Now instanciate/create the world
        val worldStorage = storage.prepareWorldStorage(name)
        world = when(worldStorage) {
            is AnvilWorldStorage -> {
                val anvilAccess = AnvilBlockAccess(worldStorage)
                World(name, worldStorage, anvilAccess)
            } else -> {
                val map = worldStorage.readBlockData()
                val access = MapBlockAccess(map)
                World(name, worldStorage, access).also {
                    it.modifiedBlocks = map
                }
            }
        }

        return world
    }

    /**
     * Get the list of worlds
     *
     * @return A copy of the current list of worlds
     */
    fun getWorlds(): List<World> {
        worldsLock.readLock().lock()
        val worlds = worlds.values.toList()
        worldsLock.readLock().unlock()

        return worlds
    }

    /**
     * Test if the specified player is banned
     *
     * @return The player ban. null if not banned
     */
    fun getPlayerBan(player: UUID): BanData? {
        return banned[player]
    }

    /**
     * Ban a specific player
     *
     * @param data The ban data to add
     * @return True if the ban data override already present ban data
     */
    fun writeBan(data: BanData): Boolean {
        val hadPrevious = (banned.put(data.uuid, data)) != null

        storage.writeBannedList(banned.values)
        return hadPrevious
    }

    /**
     * Unban a specific player
     *
     * @param player The uuid of the player to unban
     * @return Return the previous ban data. null if was not banned
     */
    fun unbanPlayer(player: UUID): BanData? {
        val data = banned.remove(player)
        // If had changes
        if (data != null) {
            storage.writeBannedList(banned.values)
        }

        return data
    }

    /**
     * Unban a specific player
     *
     * @param player The player to unban
     * @return Return the previous ban data. null if was not banned
     */
    fun unbanPlayer(player: Player): BanData? {
        return unbanPlayer(player.uuid)
    }

}

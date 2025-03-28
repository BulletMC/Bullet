package com.aznos.storage

import com.aznos.world.World
import java.util.concurrent.locks.ReentrantReadWriteLock

class StorageManager(val storage: AbstractServerStorage) {

    private val worldsLock = ReentrantReadWriteLock()
    private val worlds = HashMap<String, World>()

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

        world = World(name, worldStorage)
        worlds[name] = world

        worldsLock.writeLock().unlock()
        return world
    }

    fun getWorlds(): List<World> {
        worldsLock.readLock().lock()
        val worlds = ArrayList<World>(worlds.values)
        worldsLock.readLock().unlock()

        return worlds
    }

}
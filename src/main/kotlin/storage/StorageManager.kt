package com.aznos.storage

import com.aznos.world.World

class StorageManager(val storage: AbstractServerStorage) {

    private val worlds = HashMap<String, World>()

    /**
     * Get world with by name if exist
     *
     * @param name the world's name
     * @return the world with that name. null if do not exist
     */
    fun getWorld(name: String): World? {
        return worlds[name]
    }

    /**
     * Get world with by name.
     * Will load and create a new world if already exist
     *
     * @param name the world's name
     * @return the world with that name
     */
    fun getOrLoadWorld(name: String): World {
        if (worlds.containsKey(name)) return worlds[name]!!

        val worldStorage = storage.prepareWorldStorage(name)
        val data = worldStorage.readWorldData()

        // TODO create world from data
        return World(name)
    }


}
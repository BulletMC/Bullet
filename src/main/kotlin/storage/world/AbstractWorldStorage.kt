package com.aznos.storage.world

import com.aznos.world.World
import com.aznos.world.data.WorldData

interface AbstractWorldStorage {

    /**
     * Get the world name related to this storage
     *
     * @return The world name linked to this storage
     */
    fun getName(): String

    /**
     * Writes world data to the storage, containing information like the difficulty of the world, time of day, etc
     * so that it can be loaded back in when the server restarts
     *
     * @param data The world data to write to storage
     * @return A world storage instance from this server storage
     */
    fun writeWorldData(data: WorldData): Boolean

    /**
     * Writes world data to the storage, containing information like the difficulty of the world, time of day, etc
     * so that it can be loaded back in when the server restarts
     *
     * @param world The world to write to storage
     * @return A world storage instance from this server storage
     */
    fun writeWorldData(
        world: World
    ): Boolean {
        return writeWorldData(WorldData(world.difficulty.id, world.weather == 1, world.timeOfDay))
    }

    /**
     * Reads world data from the storage
     *
     * @return The world data that was read from the storage
     */
    fun readWorldData(): WorldData

    // todo block/chunk load & save

    /**
     * Inform the storage system that the server is closing and should try to save everything now
     *
     * @return A world storage instance from this server storage
     */
    fun notifyClose(): Boolean

}
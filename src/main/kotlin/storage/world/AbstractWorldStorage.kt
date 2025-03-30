package com.aznos.storage.world

import com.aznos.datatypes.BlockPositionType
import com.aznos.world.World
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.WorldData
import org.jetbrains.annotations.Contract

interface AbstractWorldStorage {

    /**
     * Get the world name related to this storage
     *
     * @return The world name linked to this storage
     */
    fun getName(): String

    /**
     * Reads world data from the storage if exist
     *
     * @return The world data that was read from the storage. null if do not exist
     */
    fun readWorldData(): WorldData?

    /**
     * Writes world data to the storage, containing information like the difficulty of the world, time of day, etc
     * so that it can be loaded back in when the server restarts
     * Note: this function has no guarantee that the player data is saved immediately
     *
     * @param data The world data to write to storage
     * @return Whether the operation was successful or not
     */
    fun writeWorldData(data: WorldData): Boolean

    /**
     * Writes world data to the storage, containing information like the difficulty of the world, time of day, etc
     * so that it can be loaded back in when the server restarts
     * Note: this function has no guarantee that the player data is saved immediately
     *
     * @param world The world to write to storage
     * @return Whether the operation was successful or not
     */
    fun writeWorldData(
        world: World
    ): Boolean {
        return writeWorldData(WorldData(world.difficulty.id, world.weather == 1, world.timeOfDay))
    }

    /**
     * Reads block data from the storage if exist
     *
     * @return A map of all the blocks that have been modified in the world
     */
    fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>

    /**
     * Writes block data to the storage.
     * containing information about all the blocks that have been modified in the world
     *
     * @param modifiedBlocks A map of all the blocks that have been modified in the world
     * @return Whether the operation was successful or not
     */
    fun writeBlockData(
        modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>
    ): Boolean

    fun writeEntity(entityData: EntityData): Boolean

    fun readEntities(): List<EntityData>?
}
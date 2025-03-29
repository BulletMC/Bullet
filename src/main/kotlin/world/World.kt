package com.aznos.world

import com.aznos.Bullet.shouldPersist
import com.aznos.datatypes.BlockPositionType
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import kotlinx.serialization.json.Json

/**
 * Represents a world in the game
 *
 * @param name The name of the world, default is just "world"
 *
 * @property weather The weather of the world, 0 for clear, 1 for rain
 * @property worldAge How old the world is
 * @property timeOfDay The time of day in the server, 0-24000
 * @property difficulty The difficulty of the world
 * @property modifiedBlocks A map of all the blocks that have been modified in the world
 * besides the default grass chunks that spawn in
 */
@Suppress("TooManyFunctions")
class World(
    val name: String,
    private val storage: AbstractWorldStorage
) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL
    lateinit var modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>

    init {
        loadWorldsData()
    }

    private fun loadWorldsData() {
        if (!shouldPersist) return
        val data = storage.readWorldData() ?: return

        val difficulty = Difficulty.getDifficultyFromID(data.difficulty)

        this.difficulty = difficulty
        this.weather = if (data.raining) 1 else 0
        this.timeOfDay = data.timeOfDay

        this.modifiedBlocks = storage.readBlockData()
    }

    fun save() {
        storage.writeWorldData(this)
        storage.writeBlockData(modifiedBlocks)
    }
}
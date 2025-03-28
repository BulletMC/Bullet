package com.aznos.world

import com.aznos.Bullet.shouldPersist
import com.aznos.Bullet.world
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.entity.player.data.BanData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.time.Duration

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
    var modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> = mutableMapOf()

    private val json = Json { allowStructuredMapKeys = true }

    init {
        loadWorldsData()
    }

    private fun loadWorldsData(){
        if(!shouldPersist) return
        val data = storage.readWorldData() ?: return

        val difficulty = Difficulty.getDifficultyFromID(data.difficulty)

        this.difficulty = difficulty
        this.weather = if(data.raining) 1 else 0
        this.timeOfDay = data.timeOfDay

        if(Files.exists(Paths.get("./${name}/data/blocks.json"))) {
            this.modifiedBlocks = readBlockData()
        }
    }

    /**
     * Saves the current world state
     *
     * @return Whether the operation was successful or not
     */
    private fun createFiles(): Boolean {
        createDirectoryIfNotExists(Paths.get("./$name"))
        createDirectoryIfNotExists(Paths.get("./$name/data"))

        createFileIfNotExists(Paths.get("./$name/data/blocks.json"))

        val banFile = Paths.get("./$name/data/banned_players.json")
        createFileIfNotExists(banFile)
        if(Files.size(banFile) == 0L) {
            Files.write(banFile, "[]".toByteArray())
        }

        return true
    }

    /**
     * Writes block data to the disk, containing information about all the blocks that have been modified in the world
     *
     * @param modifiedBlocks A map of all the blocks that have been modified in the world
     */
    fun writeBlockData(modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>) {
        createFiles()

        val jsonData = json.encodeToString(modifiedBlocks)
        Files.write(Paths.get("./$name/data/blocks.json"), jsonData.toByteArray())
    }

    /**
     * Reads block data from the disk
     *
     * @return A map of all the blocks that have been modified in the world
     */
    fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> {
        val path = Paths.get("./$name/data/blocks.json")
        val jsonData = Files.readString(path)
        return json.decodeFromString(jsonData)
    }

    fun writeBannedPlayer(
        player: UUID,
        reason: String,
        duration: Duration,
        moderator: UUID
    ) {
        createFiles()

        val path = Paths.get("./$name/data/banned_players.json")
        val currentBans: MutableList<BanData> = if(Files.exists(path)) {
            try {
                val jsonData = Files.readString(path)
                Json.decodeFromString(jsonData)
            } catch(e: IOException) {
                mutableListOf()
            }
        } else mutableListOf()

        currentBans.removeIf { it.uuid == player }
        currentBans.add(BanData(player, reason, duration, System.currentTimeMillis(), moderator))

        val newJson = Json.encodeToString(currentBans)
        Files.write(path, newJson.toByteArray())
    }

    fun unbanPlayer(player: UUID) {
        createFiles()

        val path = Paths.get("./$name/data/banned_players.json")
        val currentBans: MutableList<BanData> = if(Files.exists(path)) {
            try {
                val jsonData = Files.readString(path)
                Json.decodeFromString(jsonData)
            } catch(e: IOException) {
                mutableListOf()
            }
        } else mutableListOf()

        currentBans.removeIf { it.uuid == player }

        val newJson = Json.encodeToString(currentBans)
        Files.write(path, newJson.toByteArray())
    }

    fun readBannedPlayers(): List<BanData> {
        val path = Paths.get("./$name/data/banned_players.json")
        val jsonData = Files.readString(path)
        return json.decodeFromString(jsonData)
    }

    /**
     * Creates a directory if it does not already exist
     *
     * @param path The path of the directory to create
     */
    private fun createDirectoryIfNotExists(path: Path) {
        if(!Files.exists(path)) {
            Files.createDirectory(path)
        }
    }

    /**
     * Creates a file if it does not already exist
     *
     * @param path The path of the file to create
     */
    private fun createFileIfNotExists(path: Path) {
        if(!Files.exists(path)) {
            Files.createFile(path)
        }
    }

    fun save() {
        storage.writeWorldData(this)
        world.writeBlockData(world.modifiedBlocks)
    }
}
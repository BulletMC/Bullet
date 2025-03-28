package com.aznos.world

import com.aznos.Bullet
import com.aznos.entity.player.data.BanData
import com.aznos.entity.player.data.Location
import com.aznos.entity.player.data.Position
import com.aznos.world.blocks.Block
import com.aznos.world.data.Difficulty
import com.aznos.world.data.PlayerData
import com.aznos.world.data.TimeOfDay
import com.aznos.world.data.WorldData
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
class World(val name: String) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL
    var modifiedBlocks: MutableMap<Position, Int> = mutableMapOf()

    private val json = Json { allowStructuredMapKeys = true }

    /**
     * Saves the current world state
     *
     * @return Whether the operation was successful or not
     */
    private fun createFiles(): Boolean {
        createDirectoryIfNotExists(Paths.get("./$name"))
        createDirectoryIfNotExists(Paths.get("./$name/data"))
        createDirectoryIfNotExists(Paths.get("./$name/players"))

        createFileIfNotExists(Paths.get("./$name/data/world.json"))
        createFileIfNotExists(Paths.get("./$name/data/blocks.json"))

        val banFile = Paths.get("./$name/data/banned_players.json")
        createFileIfNotExists(banFile)
        if(Files.size(banFile) == 0L) {
            Files.write(banFile, "[]".toByteArray())
        }

        return true
    }

    /**
     * Writes world data to the disk, containing information like the difficulty of the world, time of day, etc
     * so that it can be loaded back in when the server restarts
     *
     * @param difficulty The difficulty of the world
     * @param raining Whether it is raining in the world or not
     * @param timeOfDay The time of day in the world
     */
    fun writeWorldData(
        difficulty: Difficulty,
        raining: Boolean,
        timeOfDay: Long,
    ) {
        createFiles()

        val worldData = WorldData(difficulty.id, raining, timeOfDay)
        val json = Json.encodeToString(worldData)
        Files.write(Paths.get("./$name/data/world.json"), json.toByteArray())
    }

    /**
     * Reads world data from the disk
     *
     * @return The world data that was read from the disk
     */
    fun readWorldData(): WorldData {
        val path = Paths.get("./$name/data/world.json")
        val json = Files.readString(path)
        return Json.decodeFromString(json)
    }

    /**
     * Writes player data to the disk, containing information like the player's location, health, food level, etc
     *
     * @param username The username of the player
     * @param uuid The UUID of the player
     * @param location The location of the player
     * @param health The health of the player
     * @param foodLevel The food level of the player
     * @param saturation The saturation level of the player
     * @param exhaustionLevel The exhaustion level of the player
     */
    fun writePlayerData(
        username: String,
        uuid: UUID,
        location: Location,
        health: Int,
        foodLevel: Int,
        saturation: Float,
        exhaustionLevel: Float,
    ) {
        createFiles()

        val playerData = PlayerData(username, uuid, location, health, foodLevel, saturation, exhaustionLevel)
        val json = Json.encodeToString(playerData)
        Files.write(Paths.get("./$name/players/$uuid.json"), json.toByteArray())
    }

    /**
     * Reads player data from the disk
     *
     * @param uuid The UUID of the player to read data for
     * @return The player data that was read from the disk
     */
    fun readPlayerData(uuid: UUID): PlayerData {
        val path = Paths.get("./$name/players/$uuid.json")
        val json = Files.readString(path)
        return Json.decodeFromString(json)
    }

    /**
     * Writes block data to the disk, containing information about all the blocks that have been modified in the world
     *
     * @param modifiedBlocks A map of all the blocks that have been modified in the world
     */
    fun writeBlockData(modifiedBlocks: MutableMap<Position, Int>) {
        createFiles()
        val newModified = mutableMapOf<Position, Int>()

        for(block in modifiedBlocks) {
            val blockObj = Block.getBlockFromID(block.value) ?: Block.AIR
            val state = Block.getStateID(blockObj)

            newModified[block.key] = state
        }

        val jsonData = json.encodeToString(newModified)
        Files.write(Paths.get("./$name/data/blocks.json"), jsonData.toByteArray())
    }

    /**
     * Reads block data from the disk
     *
     * @return A map of all the blocks that have been modified in the world
     */
    fun readBlockData(): MutableMap<Position, Int> {
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
}
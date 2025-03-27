package com.aznos.world

import com.aznos.entity.player.data.Location
import com.aznos.entity.player.data.Position
import com.aznos.world.data.Difficulty
import com.aznos.world.data.PlayerData
import com.aznos.world.data.TimeOfDay
import com.aznos.world.data.WorldData
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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

        return true
    }

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

    fun readWorldData(): WorldData {
        val path = Paths.get("./$name/data/world.json")
        val json = Files.readString(path)
        return Json.decodeFromString(json)
    }

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

    fun readPlayerData(uuid: UUID): PlayerData {
        val path = Paths.get("./$name/players/$uuid.json")
        val json = Files.readString(path)
        return Json.decodeFromString(json)
    }

    fun writeBlockData(modifiedBlocks: MutableMap<Position, Int>) {
        println(modifiedBlocks)
        createFiles()

        val jsonData = json.encodeToString(modifiedBlocks)
        Files.write(Paths.get("./$name/data/blocks.json"), jsonData.toByteArray())
    }

    fun readBlockData(): MutableMap<Position, Int> {
        val path = Paths.get("./$name/data/blocks.json")
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
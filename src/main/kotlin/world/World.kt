package com.aznos.world

import com.aznos.Bullet
import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class World(val name: String) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL

    /**
     * Saves the current world state
     *
     * @return Whether the operation was successful or not
     */
    fun saveWorld(
        difficulty: Difficulty = this.difficulty,
        raining: Boolean = false,
        timeOfDay: Long = this.timeOfDay,
    ): Boolean {
        createDirectoryIfNotExists(Paths.get("./$name"))
        createDirectoryIfNotExists(Paths.get("./$name/data"))
        createDirectoryIfNotExists(Paths.get("./$name/players"))

        createFileIfNotExists(Paths.get("./$name/data/world.json"))

        writeWorldData(difficulty, raining, timeOfDay)

        return true
    }

    private fun writeWorldData(
        difficulty: Difficulty,
        raining: Boolean,
        timeOfDay: Long,
    ) {
        val data = """
            {
                "difficulty": ${difficulty.id},
                "raining": $raining,
                "timeOfDay": $timeOfDay,
            }
        """.trimIndent()

        Files.write(Paths.get("./$name/data/world.json"), data.toByteArray())
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
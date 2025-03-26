package com.aznos.world

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
    fun saveWorld(): Boolean {
        createDirectoryIfNotExists(Paths.get("./$name"))
        createDirectoryIfNotExists(Paths.get("./$name/data"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM-1"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM-1/region"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM1"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM1/region"))
        createDirectoryIfNotExists(Paths.get("./$name/players"))
        createDirectoryIfNotExists(Paths.get("./$name/region"))

        return true
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
}
package com.aznos.world

import com.aznos.entity.player.data.Location
import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class PlayerLocation(val x: Double, val y: Double, val z: Double)

class World(val name: String) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL
    private val playerLocations: ConcurrentHashMap<UUID, PlayerLocation> = ConcurrentHashMap()

    fun updatePlayerLocation(playerId: UUID, location: Location) {
        playerLocations[playerId] = PlayerLocation(location.x, location.y, location.z)
    }

    fun getPlayerLocationByUUID(playerId: UUID): PlayerLocation? {
        return playerLocations[playerId]
    }

}
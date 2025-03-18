package com.aznos.world

import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay

class World(val name: String) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL
}
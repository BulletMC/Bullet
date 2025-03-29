package com.aznos.entity.livingentity

import com.aznos.datatypes.LocationType
import com.aznos.entity.Entity

class LivingEntity : Entity() {
    var passengers: MutableList<Int> = mutableListOf()

    var location: LocationType.Location = LocationType.Location(0.0, 0.0, 0.0, 0f, 0f)
    var velocityX: Short = 0
    var velocityY: Short = 0
    var velocityZ: Short = 0
}
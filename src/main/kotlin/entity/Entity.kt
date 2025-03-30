package com.aznos.entity

import com.aznos.datatypes.LocationType
import java.util.UUID

open class Entity {
    val entityID: Int = lastID++
    var location: LocationType.Location = LocationType.Location(0.0, 0.0, 0.0)
    open val uuid: UUID = UUID.randomUUID()

    companion object {
        private var lastID = 0
    }
}
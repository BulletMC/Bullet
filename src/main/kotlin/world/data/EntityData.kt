package com.aznos.world.data

import com.aznos.datatypes.LocationType
import com.aznos.serialization.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EntityData(
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    @Contextual val location: LocationType.Location,
    val entityType: Int,
    val health: Int = 0,
    val headPitch: Float = 0f,
    val velocityX: Short = 0,
    val velocityY: Short = 0,
    val velocityZ: Short = 0,
    val isLiving: Boolean = true
)

package com.aznos.world.data

import com.aznos.datatypes.LocationType
import com.aznos.serialization.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EntityData(
    val entityID: Int,
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val entityType: Int,
    @Contextual val location: LocationType.Location,
    val headYaw: Float = 0f,
    val velocityX: Short = 0,
    val velocityY: Short = 0,
    val velocityZ: Short = 0,
    val passengers: List<Int> = emptyList(),
    val isLiving: Boolean = true
)

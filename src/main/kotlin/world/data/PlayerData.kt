package com.aznos.world.data

import com.aznos.datatypes.LocationType
import com.aznos.entity.player.data.PermissionLevel
import com.aznos.serialization.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlayerData(
    val username: String,
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    @Contextual val location: LocationType.Location,
    val health: Int,
    val foodLevel: Int,
    val saturation: Float,
    val exhaustionLevel: Float,
    val inventory: List<Pair<Int, Int>>,
    val permissionLevel: PermissionLevel
)

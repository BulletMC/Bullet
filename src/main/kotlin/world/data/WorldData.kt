package com.aznos.world.data

import kotlinx.serialization.Serializable

@Serializable
data class WorldData(
    val difficulty: Int,
    val raining: Boolean,
    val timeOfDay: Long
)

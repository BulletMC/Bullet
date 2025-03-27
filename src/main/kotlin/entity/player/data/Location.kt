package com.aznos.entity.player.data

import kotlinx.serialization.Serializable

/**
 * Represents a location in the world
 */
@Serializable
data class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
)

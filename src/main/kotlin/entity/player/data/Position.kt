package com.aznos.entity.player.data

import kotlinx.serialization.Serializable

/**
 * Represents a block position in the world
 */
@Serializable
data class Position(
    var x: Double,
    var y: Double,
    var z: Double,
)
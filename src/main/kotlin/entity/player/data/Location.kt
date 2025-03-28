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
) {

    constructor(x: Double, y: Double, z: Double) : this(x, y, z, 0f, 0f)

    fun add(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location {
        return copy(
            x = this.x + x,
            y = this.y + y,
            z = this.z + z,
            yaw = this.yaw + yaw,
            pitch = this.pitch + pitch
        )
    }

    fun add(x: Double, y: Double, z: Double): Location {
        return copy(
            x = this.x + x,
            y = this.y + y,
            z = this.z + z,
        )
    }

    fun add(yaw: Float, pitch: Float): Location {
        return copy(
            yaw = this.yaw + yaw,
            pitch = this.pitch + pitch
        )
    }

    fun set(x: Double, y: Double, z: Double): Location {
        return copy(
            x = x,
            y = y,
            z = z
        )
    }

    fun set(yaw: Float, pitch: Float): Location {
        return copy(
            yaw = yaw,
            pitch = pitch
        )
    }

}

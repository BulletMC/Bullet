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
) {

    constructor(blockPos: Long) : this(
        (blockPos shr 38).toDouble(),
        (blockPos and 0xFFF).toDouble(),
        (blockPos shl 26 shr 38).toDouble(),
    )

    fun add(x: Double, y: Double, z: Double): Position {
        this.x += x
        this.y += y
        this.z += z

        return this
    }

}
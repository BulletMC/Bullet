package com.maddoxh.bullet.entity

import java.util.UUID

abstract class Entity(
    val entityID: Int,
    val uuid: UUID
) {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    var yaw: Float = 0.0f
    var pitch: Float = 0.0f
    var onGround: Boolean = false

    abstract fun tick()
}
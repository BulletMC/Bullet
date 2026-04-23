package com.maddoxh.bullet.entity

import java.util.UUID

abstract class LivingEntity(
    entityID: Int,
    uuid: UUID
) : Entity(entityID, uuid) {
    var health: Float = 20.0f
    var maxHealth: Float = 20.0f
    var dead: Boolean = false

    fun kill() {
        health = 0.0f
        dead = true
        onDeath()
    }

    protected open fun onDeath() {}
}
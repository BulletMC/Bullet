package com.aznos.entity.livingentity

import com.aznos.entity.Entity
import com.aznos.entity.ai.Navigator
import com.aznos.entity.ai.RandomStrollGoal
import com.aznos.util.AABB
import com.aznos.world.World

class LivingEntity : Entity() {
    private val navigatorLazy by lazy { Navigator(this) }
    val navigator get() = navigatorLazy

    private val strollLazy by lazy { RandomStrollGoal() }

    /**
     * Updates the entity's AI and navigation
     *
     * @param world The world in which the entity exists
     */
    fun tickAI(world: World) {
        strollLazy.tick(world, this)
        navigator.tick(world)
    }

    /**
     * Returns the bounding box of the entity's feet
     */
    fun feetBox() = AABB(
        location.x - 0.3, location.z - 0.3,
        location.x + 0.3, location.z + 0.3,
    )
}
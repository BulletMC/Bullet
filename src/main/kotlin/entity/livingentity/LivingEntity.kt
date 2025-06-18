package com.aznos.entity.livingentity

import com.aznos.entity.Entity
import com.aznos.entity.ai.Navigator
import com.aznos.entity.ai.RandomStrollGoal
import com.aznos.world.World

class LivingEntity : Entity() {
    private val navigatorLazy by lazy { Navigator(this) }
    val navigator get() = navigatorLazy

    private val strollLazy by lazy { RandomStrollGoal() }
    fun tickAI(world: World) {
        strollLazy.tick(world, this)
        navigator.tick(world)
    }
}
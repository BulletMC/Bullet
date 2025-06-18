package com.aznos.entity.ai

import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.Vec3D
import com.aznos.world.World

class Navigator(private val mob: LivingEntity) {
    private var target: Vec3D? = null
    private val speedPerTick = 0.25

    fun moveTo(target: Vec3D) { this.target = target }
    fun tick(world: World) {
        val dest = target ?: return
        val delta = dest - mob.location.toVec3D()

        if(delta.length() < 0.05) {
            target = null
            return
        }

        val step = delta.normalize() * speedPerTick
        mob.location = mob.location.add(step.x, step.y, step.z)

        world.broadcastEntityUpdate(mob)
    }
}
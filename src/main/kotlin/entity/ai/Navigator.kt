package com.aznos.entity.ai

import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.Vec3D
import com.aznos.world.World
import kotlin.math.atan2
import kotlin.math.round

class Navigator(private val mob: LivingEntity) {
    private fun quantise(v: Double) = round(v * 32.0) / 32.0

    private var target: Vec3D? = null
    private val speedPerTick = 0.15

    fun moveTo(target: Vec3D) { this.target = target }
    fun tick(world: World) {
        val dest = target ?: return
        val delta = dest - mob.location.toVec3D()

        if(delta.length() < 0.05) {
            val groundY = world.getHighestSolidBlockY(dest.x, dest.z)
            mob.location = mob.location.set(
                quantise(dest.x),
                quantise((groundY + 1).toDouble()),
                quantise(dest.z)
            )

            target = null
            world.broadcastEntityUpdate(mob)
            return
        }

        val step = delta.normalize() * speedPerTick
        mob.location = mob.location.add(step.x, step.y, step.z)
        mob.location.yaw = (Math.toDegrees(atan2(step.x, step.z))).toFloat()

        mob.location = mob.location.set(
            quantise(mob.location.x),
            quantise(mob.location.y),
            quantise(mob.location.z)
        )

        world.broadcastEntityUpdate(mob)
    }
}
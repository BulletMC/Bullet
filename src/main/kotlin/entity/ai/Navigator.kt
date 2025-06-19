package com.aznos.entity.ai

import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.Vec3D
import com.aznos.world.World
import kotlin.math.atan2
import kotlin.math.round
import kotlin.math.roundToInt

private const val WALK_SPEED = 0.2
private const val YAW_STEP = 360f / 256f

class Navigator(private val mob: LivingEntity) {
    private var target: Vec3D? = null

    fun moveTo(target: Vec3D) { this.target = target }
    fun tick(world: World) {
        val dest = target ?: return

        val from = mob.location.toVec3D()
        val delta = dest-from
        val dist = delta.length()

        if(dist < 0.05) {
            snapToGround(world, dest)
            target = null
            world.broadcastEntityUpdate(mob)
            return
        }

        val step = delta.normalize() * WALK_SPEED
        mob.location = mob.location.add(step.x, 0.0, step.z)

        val yawRad = atan2(-step.x, step.z)
        mob.location = mob.location.set(
            yaw = quantiseYaw(Math.toDegrees(yawRad).toFloat()),
            pitch = 0f
        )

        world.broadcastEntityUpdate(mob)
    }

    private fun snapToGround(world: World, dest: Vec3D) {
        val groundY = world.getHighestSolidBlockY(dest.x, dest.z)
        mob.location = mob.location.set(
            quantise(dest.x),
            quantise((groundY + 1).toDouble()),
            quantise(dest.z)
        )
    }

    private fun quantise(v: Double) = round(v * 32.0) / 32.0
    private fun quantiseYaw(yaw: Float) = ((yaw / YAW_STEP).roundToInt() * YAW_STEP) % 360f
}
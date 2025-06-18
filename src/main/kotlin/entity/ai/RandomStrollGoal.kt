package com.aznos.entity.ai

import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.Vec3D
import com.aznos.world.World
import kotlin.random.Random
import kotlin.random.nextInt

class RandomStrollGoal(
    private val cooldownTicks: Int = 120,
    private val radius: Int = 10,
    private val rnd: Random = Random
) {
    private var nextMove = cooldownTicks

    fun tick(world: World, mob: LivingEntity) {
        if(nextMove-- > 0) return
        nextMove = cooldownTicks + rnd.nextInt(cooldownTicks)

        val dx = rnd.nextInt(-radius, radius + 1)
        val dz = rnd.nextInt(-radius, radius + 1)
        val groundY = world.getHighestSolidBlockY(mob.location.x + dx, mob.location.z + dz)
        val dest = Vec3D(Triple(mob.location.x + dx, groundY + 1.0, mob.location.z + dz))

        mob.navigator.moveTo(dest)
    }
}
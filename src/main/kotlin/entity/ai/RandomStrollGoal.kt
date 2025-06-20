package com.aznos.entity.ai

import com.aznos.datatypes.BlockPositionType
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.BlockUtils
import com.aznos.util.Vec3D
import com.aznos.world.World
import kotlin.random.Random

class RandomStrollGoal(
    private val cooldownTicks: Int = 120,
    private val radius: Int = 10,
    private val rnd: Random = Random
) {
    private var nextMove = cooldownTicks

    fun tick(world: World, mob: LivingEntity) {
        if(nextMove-- > 0) return
        nextMove = cooldownTicks + rnd.nextInt(cooldownTicks)

        val targetX = mob.location.x + rnd.nextInt(-radius, radius + 1)
        val targetZ = mob.location.z + rnd.nextInt(-radius, radius + 1)
        val groundY = world.getHighestSolidBlockY(targetX, targetZ)
        if(groundY < 0) return

        val checkPos = BlockPositionType.BlockPosition(targetX, groundY + 1.0, targetZ).toBlockPosI()
        if(!BlockUtils.isPassable(checkPos.toBlockPos(), world)) return

        mob.navigator.moveTo(Vec3D(Triple(targetX, groundY + 1.0, targetZ)))
    }
}
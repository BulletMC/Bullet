package com.aznos.entity.ai

import com.aznos.datatypes.BlockPositionType
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.util.BlockUtils
import com.aznos.util.Vec3D
import com.aznos.world.World
import kotlin.math.atan2
import kotlin.math.round
import kotlin.math.roundToInt

private const val WALK_SPEED = 0.2
private const val YAW_STEP = 360f / 256f

class Navigator(private val mob: LivingEntity) {
    private var target: Vec3D? = null

    fun moveTo(dest: Vec3D, world: World) {
        target = quantiseVec(dest, world)
    }

    fun tick(world: World) {
        val dest = target ?: return

        val from = mob.location.toVec3D()
        val delta = dest - from
        val dist = delta.length()

        if(dist < 0.05) {
            target = null
            return
        }

        val step = delta.normalize() * WALK_SPEED
        val nx = mob.location.x + step.x
        val nz = mob.location.z + step.z
        var ny = world.getHighestSolidBlockY(nx, nz) + 1

        val ahead = BlockPositionType.BlockPosition(nx, ny.toDouble(), nz)
        if(!BlockUtils.isPassable(ahead, world)) {
            val headClear = BlockUtils.isClearHeadroom(BlockPositionType.BlockPosition(nx, ny.toDouble(), nz), world)
            val floorBelow = BlockUtils.isSolid(BlockPositionType.BlockPosition(nx, (ny - 1).toDouble(), nz), world)

            if(headClear && floorBelow) {
                ny += 1
            } else {
                target = null
                return
            }
        }

        mob.location = mob.location.set(nx, ny.toDouble(), nz)

        val yawRad = atan2(-step.x, step.z)
        mob.location = mob.location.set(
            yaw = quantiseYaw(Math.toDegrees(yawRad).toFloat()),
            pitch = 0f
        )

        pushOutOfBlock(world)
        world.broadcastEntityUpdate(mob)
    }

    private fun pushOutOfBlock(world: World) {
        if(target == null) return

        val y = mob.location.y.toInt()
        val box = mob.feetBox()

        if(!box.intersectsSolid(world, y)) return
        val offsets = listOf(
            Vec3D(Triple(-0.1, 0.0,  0.0)),
            Vec3D(Triple( 0.1, 0.0,  0.0)),
            Vec3D(Triple( 0.0, 0.0, -0.1)),
            Vec3D(Triple( 0.0, 0.0,  0.1))
        )

        for(off in offsets) {
            val nx = mob.location.x + off.x
            val nz = mob.location.z + off.z
            if(BlockUtils.isPassable(BlockPositionType.BlockPosition(nx, y.toDouble(), nz), world)) {
                mob.location = mob.location.add(off.x, 0.0, off.z)
                return
            }
        }
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

    private fun quantiseVec(dest: Vec3D, world: World): Vec3D {
        val y = world.getHighestSolidBlockY(dest.x, dest.z) + 1
        return Vec3D(
            Triple(
                quantise(dest.x),
                quantise(y.toDouble()),
                quantise(dest.z)
            )
        )
    }
}
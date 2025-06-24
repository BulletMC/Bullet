package com.aznos.util

import com.aznos.datatypes.BlockPositionType
import com.aznos.world.World
import kotlin.math.sqrt

@JvmInline value class Vec3D(val raw: Triple<Double, Double, Double>) {
    val x get() = raw.first
    val y get() = raw.second
    val z get() = raw.third

    operator fun plus(o: Vec3D) = Vec3D(Triple(x + o.x, y + o.y, z + o.z))
    operator fun minus(o: Vec3D) = Vec3D(Triple(x - o.x, y - o.y, z - o.z))
    operator fun times(d: Double) = Vec3D(Triple(x * d, y * d, z * d))

    fun length() = sqrt(x * x + y * y + z * z)
    fun normalize() = if(length() == 0.0) this else Vec3D(Triple(x / length(), y / length(), z / length()))
}

/**
 * Represents an Axis-Aligned Bounding Box (AABB) in a 3D space
 * with minimum and maximum coordinates in the X and Z dimensions
 */
data class AABB(val minX: Double, val minZ: Double, val maxX: Double, val maxZ: Double) {
    /**
     * Checks if this AABB intersects with a solid block at the given Y coordinate in the specified world
     *
     * @param world The world in which to check for solid blocks
     * @param y The Y coordinate to check for solid blocks
     * @return True if the AABB intersects with a solid block at the given Y coordinate, false otherwise
     */
    fun intersectsSolid(world: World, y: Int): Boolean {
        var x = minX
        while(x <= maxX) {
            var z = minZ
            while(z <= maxZ) {
                if(BlockUtils.isSolid(BlockPositionType.BlockPosition(x, y.toDouble(), z), world)) return true
                z += 0.5
            }
            x += 0.5
        }

        return false
    }
}
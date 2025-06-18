package com.aznos.util

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
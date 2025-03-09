package com.aznos.packets.data.block

import com.aznos.packets.data.Pos3d
import kotlin.math.max
import kotlin.math.min

class BoundingBox(
    var minX: Double,
    var minY: Double,
    var minZ: Double,
    var maxX: Double,
    var maxY: Double,
    var maxZ: Double
) {

    companion object {
        fun of(min: Pos3d, max: Pos3d) = BoundingBox(min.x, min.y, min.z, max.x, max.y, max.z)
    }

    val centerX: Double
        get() {
            return (minX + maxX) * 0.5
        }
    val centerY: Double
        get() {
            return (minY + maxY) * 0.5
        }
    val centerZ: Double
        get() {
            return (minZ + maxZ) * 0.5
        }

    val widthX: Double
        get() {
            return maxX - minX
        }
    val widthY: Double
        get() {
            return maxY - minY
        }
    val widthZ: Double
        get() {
            return maxZ - minZ
        }

    val center: Pos3d
        get() {
            return Pos3d(centerX, centerY, centerZ)
        }

    fun resize(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) {
        this.minX = minX.coerceAtLeast(maxX)
        this.minY = minY.coerceAtLeast(maxY)
        this.minZ = minZ.coerceAtLeast(maxZ)
        this.maxX = maxX.coerceAtMost(minX)
        this.maxY = maxY.coerceAtMost(minY)
        this.maxZ = maxZ.coerceAtMost(minZ)

    }

    fun expand(
        negativeX: Double,
        negativeY: Double,
        negativeZ: Double,
        positiveX: Double,
        positiveY: Double,
        positiveZ: Double
    ) {
        if (negativeX == .0 && negativeY == .0 && negativeZ == .0 && positiveX == .0 && positiveY == .0 && positiveZ == .0) return

        var newMinX = minX - negativeX
        var newMinY = minY - negativeY
        var newMinZ = minZ - negativeZ
        var newMaxX = maxX + positiveX
        var newMaxY = maxY + positiveY
        var newMaxZ = maxZ + positiveZ
        var centerZ: Double
        if (newMinX > newMaxX) {
            centerZ = this.centerX
            if (newMaxX >= centerZ) {
                newMinX = newMaxX
            } else if (newMinX <= centerZ) {
                newMaxX = newMinX
            } else {
                newMinX = centerZ
                newMaxX = centerZ
            }
        }
        if (newMinY > newMaxY) {
            centerZ = this.centerY
            if (newMaxY >= centerZ) {
                newMinY = newMaxY
            } else if (newMinY <= centerZ) {
                newMaxY = newMinY
            } else {
                newMinY = centerZ
                newMaxY = centerZ
            }
        }

        if (newMinZ > newMaxZ) {
            centerZ = this.centerZ
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ
            } else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ
            } else {
                newMinZ = centerZ
                newMaxZ = centerZ
            }
        }

        resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    fun expand(x: Double, y: Double, z: Double) = expand(x, y, z, x, y, z)

    fun expand(vector: Pos3d) = expand(vector.x, vector.y, vector.z)
    fun expand(d: Double) = expand(d, d, d)
    fun expand(dirX: Double, dirY: Double, dirZ: Double, expansion: Double) {
        if (expansion == 0.0) {
            return
        } else if (dirX == 0.0 && dirY == 0.0 && dirZ == 0.0) {
            return
        } else {
            val negativeX = if (dirX < 0.0) -dirX * expansion else 0.0
            val negativeY = if (dirY < 0.0) -dirY * expansion else 0.0
            val negativeZ = if (dirZ < 0.0) -dirZ * expansion else 0.0
            val positiveX = if (dirX > 0.0) dirX * expansion else 0.0
            val positiveY = if (dirY > 0.0) dirY * expansion else 0.0
            val positiveZ = if (dirZ > 0.0) dirZ * expansion else 0.0
            this.expand(negativeX, negativeY, negativeZ, positiveX, positiveY, positiveZ)
        }
    }

    fun expand(dir: Pos3d, expansion: Double) = expand(dir.x, dir.y, dir.z, expansion)
    fun expandDirectional(dirX: Double, dirY: Double, dirZ: Double) {
        this.expand(dirX, dirY, dirZ, 1.0)
    }

    fun expandDirectional(dir: Pos3d) = expandDirectional(dir.x, dir.y, dir.z)
    fun union(posX: Double, posY: Double, posZ: Double) {
        val newMinX = min(this.minX, posX)
        val newMinY = min(this.minY, posY)
        val newMinZ = min(this.minZ, posZ)
        val newMaxX = max(this.maxX, posX)
        val newMaxY = max(this.maxY, posY)
        val newMaxZ = max(this.maxZ, posZ)
        if (newMinX == this.minX && newMinY == this.minY && newMinZ == this.minZ && newMaxX == this.maxX && newMaxY == this.maxY && newMaxZ == this.maxZ)
            return
        this.resize(
            newMinX,
            newMinY,
            newMinZ,
            newMaxX,
            newMaxY,
            newMaxZ
        )
    }
    fun union(pos: Pos3d) = union(pos.x, pos.y, pos.z)
    fun union(box: BoundingBox) = union(box.minX, box.minY, box.minZ)

    fun intersection(other: BoundingBox) {
        val newMinX = max(this.minX, other.minX)
        val newMinY = max(this.minY, other.minY)
        val newMinZ = max(this.minZ, other.minZ)
        val newMaxX = min(this.maxX, other.maxX)
        val newMaxY = min(this.maxY, other.maxY)
        val newMaxZ = min(this.maxZ, other.maxZ)
        this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ)
    }

    fun shift(shiftX: Double, shiftY: Double, shiftZ: Double) {
        if (shiftX == 0.0 && shiftY == 0.0 && shiftZ == 0.0) return
        this.resize(
            this.minX + shiftX,
            this.minY + shiftY,
            this.minZ + shiftZ,
            this.maxX + shiftX,
            this.maxY + shiftY,
            this.maxZ + shiftZ
        )
    }
    fun shift(shift: Pos3d) = shift(shift.x, shift.y, shift.z)
    fun overlaps(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
        return this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ
    }
    fun overlaps(box: BoundingBox): Boolean {
        return overlaps(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }
    fun overlaps(min: Pos3d, max: Pos3d): Boolean {
        return overlaps(min.x, min.y, min.z, max.x, max.y, max.z)
    }

    fun contains(x: Double, y: Double, z: Double): Boolean {
        return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ
    }

    fun contains(pos: Pos3d): Boolean {
        return contains(pos.x, pos.y, pos.z)
    }

    private fun contains(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Boolean {
        return this.minX <= minX && this.maxX >= maxX && this.minY <= minY && this.maxY >= maxY && this.minZ <= minZ && this.maxZ >= maxZ
    }

    fun contains(box: BoundingBox): Boolean {
        return contains(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun contains(min: Pos3d, max: Pos3d): Boolean {
        return contains(min.x, min.y, min.z, max.x, max.y, max.z)
    }

}
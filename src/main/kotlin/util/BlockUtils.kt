package com.aznos.util

import com.aznos.world.blocks.Block
import com.aznos.world.data.Axis
import com.aznos.world.items.Item
import kotlin.math.abs

object BlockUtils {
    fun getCardinalDirection(yaw: Float): String {
        val rot = (yaw % 360 + 360) % 360
        return when {
            rot >= 45 && rot < 135 -> "west"
            rot >= 135 && rot < 225 -> "north"
            rot >= 225 && rot < 315 -> "east"
            else -> "south"
        }
    }

    fun getRotationalDirection(yaw: Float): Int {
        val normalizedYaw = (yaw % 360 + 360) % 360
        return ((normalizedYaw / 22.5).toInt() % 16)
    }

    fun getAxisDirection(yaw: Float, pitch: Float): Axis {
        return when {
            pitch > 60f -> Axis.Y
            pitch < -60f -> Axis.Y
            abs(yaw) % 180 < 45 || abs(yaw) % 180 > 135 -> Axis.Z
            else -> Axis.X
        }
    }

    fun getStateID(block: Any, properties: Map<String, String>): Int {
        return when (block) {
            is Block -> Block.getStateID(block, properties)
            is Item -> Item.getStateID(block, properties)
            else -> -1
        }
    }
}
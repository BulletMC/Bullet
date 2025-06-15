package com.aznos.util

import com.aznos.ClientSession
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.Axis
import com.aznos.world.items.Item
import kotlin.math.abs
import kotlin.math.ceil

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

    fun getBlockBreakTime(client: ClientSession, block: Any): Long {
        val player = client.player
        val heldItem = player.getHeldItem().item
        val blockObj = when (block) {
            is Block -> block
            is Item -> Block.getBlockFromID(block.id) ?: return 1
            else -> return 0
        }

        val hardness = blockObj.hardness
        if (hardness <= 0) return 0

        val isBestTool = getBestTool(blockObj, heldItem)
        val canHarvest = canHarvestBlock(blockObj, heldItem)
        val toolMultiplier = getToolMultiplier(heldItem)

        var speedMultiplier = if (isBestTool) toolMultiplier else 1.0
        if(!player.onGround) {
            speedMultiplier /= 5.0
        }

        var damage = speedMultiplier / hardness
        damage /= if (canHarvest) {
            30.0
        } else {
            100.0
        }

        if(damage > 1.0) return 0
        val ticks = ceil(1.0 / damage)
        val seconds = ticks / 20.0

        return seconds.toLong()
    }

    fun getToolMultiplier(heldItem: Item): Double =
        when(heldItem) {
            Item.WOODEN_PICKAXE, Item.WOODEN_AXE, Item.WOODEN_SHOVEL, Item.WOODEN_HOE -> 2.0
            Item.STONE_PICKAXE, Item.STONE_AXE, Item.STONE_SHOVEL, Item.STONE_HOE -> 4.0
            Item.IRON_PICKAXE, Item.IRON_AXE, Item.IRON_SHOVEL, Item.IRON_HOE -> 6.0
            Item.GOLDEN_PICKAXE, Item.GOLDEN_AXE, Item.GOLDEN_SHOVEL, Item.GOLDEN_HOE -> 12.0
            Item.DIAMOND_PICKAXE, Item.DIAMOND_AXE, Item.DIAMOND_SHOVEL, Item.DIAMOND_HOE -> 8.0
            Item.NETHERITE_PICKAXE, Item.NETHERITE_AXE, Item.NETHERITE_SHOVEL, Item.NETHERITE_HOE -> 9.0
            else -> 1.0
        }

    fun canHarvestBlock(blockObj: Block, heldItem: Item): Boolean =
        canHarvestRock(blockObj, heldItem) || canHarvestMetal(blockObj, heldItem)

    @Suppress("ComplexCondition")
    private fun canHarvestRock(blockObj: Block, heldItem: Item): Boolean =
        BlockTags.ROCK_1.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_HAND.contains(heldItem) ||

                BlockTags.ROCK_2.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_WOODEN.contains(heldItem) ||

                BlockTags.ROCK_3.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_STONE.contains(heldItem) ||

                BlockTags.ROCK_4.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_IRON.contains(heldItem)

    @Suppress("ComplexCondition")
    private fun canHarvestMetal(blockObj: Block, heldItem: Item): Boolean =
        BlockTags.METAL_1.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_HAND.contains(heldItem) ||

                BlockTags.METAL_2.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_WOODEN.contains(heldItem) ||

                BlockTags.METAL_3.contains(blockObj) &&
                BlockTags.TOOLS.contains(heldItem) &&
                BlockTags.ABOVE_STONE.contains(heldItem)

    private fun getBestTool(blockObj: Block, heldItem: Item): Boolean =
        when {
            BlockTags.PICKAXE.contains(blockObj) && BlockTags.PICKAXES.contains(heldItem) -> true
            BlockTags.AXE.contains(blockObj) && BlockTags.AXES.contains(heldItem) -> true
            BlockTags.SHOVEL.contains(blockObj) && BlockTags.SHOVELS.contains(heldItem) -> true
            BlockTags.SWORD.contains(blockObj) && BlockTags.SWORDS.contains(heldItem) -> true
            else -> false
        }
}
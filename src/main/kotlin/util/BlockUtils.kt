package com.aznos.util

import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.world.World
import com.aznos.world.blocks.Block
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.Axis
import com.aznos.world.items.Item
import kotlin.math.abs
import kotlin.math.ceil

@Suppress("TooManyFunctions")
object BlockUtils {
    /**
     * Returns the cardinal direction based on the yaw angle.
     *
     * @param yaw The yaw angle in degrees.
     * @return The cardinal direction as a string ("north", "south", "east", "west").
     */
    fun getCardinalDirection(yaw: Float): String {
        val rot = (yaw % 360 + 360) % 360
        return when {
            rot >= 45 && rot < 135 -> "west"
            rot >= 135 && rot < 225 -> "north"
            rot >= 225 && rot < 315 -> "east"
            else -> "south"
        }
    }

    /**
     * Returns the rotational direction based on the yaw angle.
     *
     * @param yaw The yaw angle in degrees.
     * @return An integer representing the rotational direction (0-15).
     */
    fun getRotationalDirection(yaw: Float): Int {
        val normalizedYaw = (yaw % 360 + 360) % 360
        return ((normalizedYaw / 22.5).toInt() % 16)
    }

    /**
     * Returns the axis direction based on the yaw and pitch angles.
     *
     * @param yaw The yaw angle in degrees.
     * @param pitch The pitch angle in degrees.
     * @return The axis direction as an Axis enum value (X, Y, or Z).
     */
    fun getAxisDirection(yaw: Float, pitch: Float): Axis {
        return when {
            pitch > 60f -> Axis.Y
            pitch < -60f -> Axis.Y
            abs(yaw) % 180 < 45 || abs(yaw) % 180 > 135 -> Axis.Z
            else -> Axis.X
        }
    }

    /**
     * Returns the state ID for a block or item based on its properties.
     *
     * @param block The block or item object.
     * @param properties A map of properties to determine the state ID.
     * @return The state ID as an integer, or -1 if not applicable.
     */
    fun getStateID(block: Any, properties: Map<String, String>): Int {
        return when (block) {
            is Block -> Block.getStateID(block, properties)
            is Item -> Item.getStateID(block, properties)
            else -> -1
        }
    }

    /**
     * Calculates the time required to break a block based on the player's held item and block properties.
     *
     * @param client The client session containing the player information.
     * @param block The block or item to be broken.
     * @return The time in seconds required to break the block, or 0 if it cannot be broken.
     */
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

    /**
     * Returns the multiplier for the tool based on its type.
     *
     * @param heldItem The item being held by the player.
     * @return The multiplier as a Double.
     */
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

    /**
     * Checks if the player can harvest a block based on the held item and block properties.
     *
     * @param blockObj The block object to be harvested.
     * @param heldItem The item being held by the player.
     * @return True if the block can be harvested, false otherwise.
     */
    fun canHarvestBlock(blockObj: Block, heldItem: Item): Boolean {
        val isRockOrMetal =
            BlockTags.ROCK_1.contains(blockObj) ||
            BlockTags.ROCK_2.contains(blockObj) ||
            BlockTags.ROCK_3.contains(blockObj) ||
            BlockTags.ROCK_4.contains(blockObj) ||
            BlockTags.METAL_1.contains(blockObj) ||
            BlockTags.METAL_2.contains(blockObj) ||
            BlockTags.METAL_3.contains(blockObj)

        return if(!isRockOrMetal) true
        else canHarvestRock(blockObj, heldItem) || canHarvestMetal(blockObj, heldItem)
    }

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

    /**
     * Checks if a block at the given position is solid (not air)
     *
     * @param blockPos The position of the block to check
     * @param world The world in which the block is located
     * @return True if the block is solid, false if it is air
     */
    fun isSolid(
        blockPos: BlockPositionType.BlockPosition, world: World
    ): Boolean {
        val bp = blockPos.toBlockPosI()
        val id = world.modifiedBlocks[bp.toBlockPos()]?.blockID ?:
            if(bp.y == 0) Block.GRASS_BLOCK.id else Block.AIR.id
        return id != Block.AIR.id
    }

    /**
     * Checks if there is a 2-block high air column at the given position
     *
     * @param blockPos The position of the block to check
     * @param world The world in which the block is located
     * @return True if the block is passable (2 blocks of air above it), false otherwise
     */
    fun isPassable(blockPos: BlockPositionType.BlockPosition, world: World): Boolean {
        val pos = blockPos.toBlockPosI()
        val air = { p: BlockPositionType.BlockPosI -> !isSolid(p.toBlockPos(), world) }

        return  air(pos) &&
                air(BlockPositionType.BlockPosI(pos.x, pos.y + 1, pos.z)) &&
                !air(BlockPositionType.BlockPosI(pos.x, pos.y - 1, pos.z))
    }

    /**
     * Finds the first solid block below the given position
     *
     * @param blockPos The position to start searching from
     * @param world The world in which the block is located
     * @return The y-coordinate of the first solid block below the given position, or -1 if none found
     */
    fun firstSolidBelow(blockPos: BlockPositionType.BlockPosition, world: World): Int {
        var y0 = blockPos.y.toInt()
        while(y0 >= 0 && !isSolid(BlockPositionType.BlockPosition(blockPos.x, y0.toDouble(), blockPos.z), world)) {
            y0--
        }

        return y0
    }

    /**
     * Checks if there is clear headroom above the given block position
     *
     * @param blockPos The position of the block to check
     * @param world The world in which the block is located
     * @return True if there is no solid block directly above, false otherwise
     */
    fun isClearHeadroom(blockPos: BlockPositionType.BlockPosition, world: World) =
        !isSolid(BlockPositionType.BlockPosition(blockPos.x, (blockPos.y + 1).toDouble(), blockPos.z), world)
}
package com.aznos.world.blocks

import com.aznos.datatypes.BlockPositionType
import com.aznos.world.data.BlockWithMetadata

/**
 * Interface for accessing and modifying blocks in the world
 */
interface BlockAccess {
    fun getBlockID(x: Int, y: Int, z: Int): Int
    fun setBlockID(x: Int, y: Int, z: Int, blockID: Int)
}

/**
 * BlockAccess implementation that uses a mutable map to store block data.
 * This allows for easy modification of blocks in the world.
 *
 * @param modified A mutable map that holds the block positions and their corresponding BlockWithMetadata.
 */
class MapBlockAccess(private val modified: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>) : BlockAccess {
    /**
     * Gets the block ID at the specified coordinates
     *
     * @param x The x-coordinate of the block
     * @param y The y-coordinate of the block
     * @param z The z-coordinate of the block
     * @return The block ID at the specified coordinates, or GRASS_BLOCK if y is 0, or AIR if not modified
     */
    override fun getBlockID(x: Int, y: Int, z: Int): Int {
        val pos = BlockPositionType.BlockPosition(x.toDouble(), y.toDouble(), z.toDouble())
        return modified[pos]?.blockID ?: if(y == 0) Block.GRASS_BLOCK.id else Block.AIR.id
    }

    /**
     * Sets the block ID at the specified coordinates
     *
     * @param x The x-coordinate of the block
     * @param y The y-coordinate of the block
     * @param z The z-coordinate of the block
     * @param blockID The ID of the block to set
     */
    override fun setBlockID(x: Int, y: Int, z: Int, blockID: Int) {
        val pos = BlockPositionType.BlockPosition(x.toDouble(), y.toDouble(), z.toDouble())
        modified[pos] = BlockWithMetadata(blockID, 0)
    }
}
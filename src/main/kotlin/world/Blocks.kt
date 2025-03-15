package com.aznos.world

/**
 * A collection of all the blocks in the game
 */
enum class Blocks(val id: Int, val string: String) {
    AIR(0, "air"),
    STONE(1, "stone"),
    GRANITE(2, "granite");

    @Suppress("unused")
    companion object {
        /**
         * Returns a block from its ID
         *
         * @param id The ID of the block
         * @return The block with the given ID
         */
        fun getBlockFromID(id: Int): Blocks {
            for(block in entries) {
                if(block.id == id) {
                    return block
                }
            }

            return AIR
        }

        /**
         * Returns a block from its string
         *
         * @param string The string of the block
         * @return The block with the given string
         */
        fun getBlockFromString(string: String): Blocks {
            for(block in entries) {
                if(block.string == string) {
                    return block
                }
            }

            return AIR
        }
    }
}
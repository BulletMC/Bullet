package com.aznos.world.data

/**
 * Represents the status of a block when a player interacts with it
 */
enum class BlockStatus(val id: Int) {
    STARTED_DIGGING(0),
    CANCELLED_DIGGING(1),
    FINISHED_DIGGING(2),
    DROP_ITEM_STACK(3),
    DROP_ITEM(4),
    SHOOT_ARROW(5),
    SWAP_ITEM(6);
}
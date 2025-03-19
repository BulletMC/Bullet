package com.aznos.entity.player

import com.aznos.packets.play.out.ServerHeldItemChangePacket
import com.aznos.world.blocks.Block

class Inventory {
    val items: MutableMap<Int, Int> = mutableMapOf()

    /**
     * Returns the players held item
     *
     * @return The item ID
     */
    fun getHeldItem(slot: Int): Int {
        val slotIndex = slot + 36
        return items[slotIndex] ?: 0
    }

    /**
     * Sets the players held item slot in the hotbar (0-8)
     *
     * @param slot The slot to select
     */
    fun setHeldSlot(slot: Int, itemID: Int) {
        items[slot] = itemID
    }
}
package com.aznos.entity.player

import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack

/**
 * Represents the player's inventory
 *
 * @property items A map of slot numbers to ItemStacks
 */
class Inventory {
    val items: MutableMap<Int, ItemStack> = mutableMapOf()

    fun get(slot: Int): ItemStack? = items[slot]
    fun set(slot: Int, stack: ItemStack?) {
        if(stack == null || stack.isAir) items.remove(slot)
        else items[slot] = stack
    }

    fun clear() = items.clear()

    fun heldStack(heldSlot: Int): ItemStack =
        items[heldSlot + 36] ?: ItemStack.of(Item.AIR)

    fun setHeldSlot(heldSlot: Int, stack: ItemStack?) =
        set(heldSlot + 36, stack)
}
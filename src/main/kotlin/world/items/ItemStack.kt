package com.aznos.world.items

import com.aznos.datatypes.Slot
import com.aznos.serialization.CompoundTagSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.querz.nbt.tag.ByteTag
import net.querz.nbt.tag.CompoundTag
import kotlin.math.min

/**
 * A stack of items, with optional metadata
 *
 * @param item The type of item in the stack
 * @param count The number of items in the stack, default is 1. You're not capped to just 64 items per stack.
 * @param damage The damage value of the item, 0 = brand new
 * @param displayName Optional custom display name
 * @param lore Optional list of components to display as lore
 * @param nbt Any optional NBT data associated with the item stack
 */
@Serializable
data class ItemStack(
    val item: Item,
    var count: Int = 1,
    var damage: Int = 0,
    var displayName: Component? = null,
    var lore: List<Component> = emptyList(),
    @Serializable(with = CompoundTagSerializer::class)
    var nbt: CompoundTag? = null
) {
    val id: Int
        get() = item.id

    var rawID: Int? = null

    fun setUnbreakable(value: Boolean = true): ItemStack {
        ensureNBT().put("Unbreakable", ByteTag(if(value) 1 else 0))
        return this
    }

    /**
     * Checks if the item stack is empty or air
     *
     * @return True if the item is air or the count is 0, false otherwise
     */
    val isAir: Boolean
        get() = item == Item.AIR || count <= 0

    /**
     * Creates a copy of an ItemStack
     *
     * @return A new ItemStack with the same properties
     */
    fun copy() = ItemStack(
        item, count, damage,
        displayName,
        lore.toMutableList(),
        nbt?.clone() as? net.querz.nbt.tag.CompoundTag ?: net.querz.nbt.tag.CompoundTag()
    ).apply { rawID = this@ItemStack.rawID }

    /**
     * Remove [amount] items and returns them in a new stack
     * If you ask for more items than available, it will return all available items that are left
     *
     * @param amount The number of items to split off from the stack
     * @return A new ItemStack containing the split items
     */
    fun split(amount: Int): ItemStack {
        require(amount > 0) { "Amount must be positive"}

        val taken = min(amount, count)
        count -= taken

        return ItemStack(
            item, taken, damage,
            displayName, lore.toMutableList(),
            nbt?.clone() as net.querz.nbt.tag.CompoundTag)
    }

    /**
     * Attempts to merge [other] into this stack
     *
     * @param other The ItemStack to merge into this one
     * @param maxPerStack The maximum number of items allowed in a single stack, default is 64
     * @return The number of items successfully merged from [other] into this stack
     */
    fun merge(other: ItemStack, maxPerStack: Int = 64): Int {
        if(other.isAir || other.item != item || other.damage != damage) return 0

        val space = maxPerStack - count
        if(space <= 0) return 0

        val moved = min(space, other.count)
        count += moved
        other.count -= moved

        return moved
    }

    fun withName(name: Component) = apply { displayName = name }
    fun withLore(vararg lines: Component) = apply { lore = lines.toMutableList() }
    fun withDamage(value: Int) = apply { damage = value }

    fun toNBT(): CompoundTag {
        val base = ItemStackNBTCodec.toNbt(this)

        nbt?.let { custom ->
            for((key, tag) in custom) base.put(key, tag.clone())
        }

        return base
    }

    fun toSlotData(): Slot.SlotData {
        val valid = (rawID != null) || (item != Item.AIR)
        val id = rawID ?: item.id
        val hasCustomData = displayName != null || lore.isNotEmpty() || damage != 0 || nbt != null

        return Slot.SlotData(
            present = valid,
            itemID = if(valid) id else 0,
            itemCount = if(valid) count.toByte() else 0,
            nbt = if(valid && hasCustomData) toNBT() else null
        )
    }

    val isStackable: Boolean
        get() = Item.isStackable(item)

    val maxStackSize: Int
        get() = Item.getMaxStackSize(item)

    private fun ensureNBT(): CompoundTag {
        if(nbt == null) nbt = CompoundTag()
        return nbt!!
    }

    companion object {
        @JvmStatic fun of(item: Item, count: Int = 1) = ItemStack(item, count)
        @JvmStatic fun empty() = ItemStack(Item.AIR, 0)
        @JvmStatic fun fromNBT(tag: net.querz.nbt.tag.CompoundTag?) = ItemStackNBTCodec.fromNbt(tag)
        @JvmStatic fun opaque(rawID: Int, count: Int) = ItemStack(Item.AIR, count).apply { this.rawID = rawID }
    }
}
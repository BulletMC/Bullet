package com.aznos.world.items

import net.querz.nbt.tag.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object ItemStackNBTCodec {
    fun fromNbt(root: CompoundTag?): ItemStack {
        if(root == null) return ItemStack(Item.AIR)

        val item = Item.getItemFromID(root.getInt("id")) ?: Item.AIR
        val count = root.getByte("Count").toInt()
        val damage = root.getInt("Damage")

        var nameComponent : Component?      = null
        var loreComponents: List<Component> = emptyList()

        root.getCompoundTag("tag")
            ?.getCompoundTag("display")
            ?.let { display ->
                display.getString("Name")?.let { json ->
                    nameComponent = MiniMessage.miniMessage().deserialize(json)
                }

                val loreTag = display.getListTag("Lore") as? ListTag<*>
                loreComponents = loreTag
                    ?.filterIsInstance<StringTag>()
                    ?.map { LegacyComponentSerializer.legacySection()
                        .deserialize(it.value) } ?: emptyList()
            }

        return ItemStack(item, count, damage, nameComponent, loreComponents, root)
    }

    fun toNbt(stack: ItemStack): CompoundTag = CompoundTag().apply {
        if(stack.damage != 0) putInt("Damage", stack.damage)
        if(stack.displayName != null || stack.lore.isNotEmpty()) {
            val display = CompoundTag()

            stack.displayName?.let { comp ->
                val json = GsonComponentSerializer.gson().serialize(comp)
                display.putString("Name", json)
            }

            if(stack.lore.isNotEmpty()) {
                val loreList = ListTag(StringTag::class.java)
                stack.lore.forEach { line ->
                    val json = GsonComponentSerializer.gson().serialize(line)
                    loreList.add(StringTag(json))
                }

                display.put("Lore", loreList)
            }

            put("display", display)
        }
    }
}
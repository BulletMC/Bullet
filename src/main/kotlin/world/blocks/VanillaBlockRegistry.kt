package com.aznos.world.blocks

object VanillaBlockRegistry {
    fun toInternal(name: String): Int {
        val base = name.removePrefix("minecraft:").uppercase()
        return Block.entries.firstOrNull { it.name == base }?.id ?: Block.AIR.id
    }

    fun toVanilla(id: Int): String = Block.entries.firstOrNull { it.id == id }
        ?.let { "minecraft:${it.name.lowercase()}" }
        ?: "minecraft:air"
}
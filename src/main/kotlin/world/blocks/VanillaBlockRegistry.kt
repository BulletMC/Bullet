package com.aznos.world.blocks

import java.util.Locale

object VanillaBlockRegistry {
    fun toInternal(name: String): Int {
        for(block in Block.entries) {
            if(block.name.equals(name, ignoreCase = true)) {
                return block.id
            }
        }

        return Block.AIR.id
    }

    fun toVanilla(id: Int): String {
        for(block in Block.entries) {
            if(block.id == id) {
                return "minecraft:${block.name.lowercase(Locale.getDefault())}"
            }
        }

        return "minecraft:air"
    }
}
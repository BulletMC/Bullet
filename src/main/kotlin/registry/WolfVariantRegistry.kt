package com.aznos.registry

import com.aznos.data.WolfVariants
import com.aznos.packets.newPacket.ResourceLocation
import dev.dewy.nbt.tags.collection.CompoundTag

class WolfVariantRegistry : Registry<WolfVariants>(ResourceLocation.vanilla("wolf_variant")) {

    init {
        for (entry in WolfVariants.entries) {
            register(ResourceLocation.vanilla(entry.name.lowercase()), entry)
        }
    }

    override fun asCompound(value: WolfVariants): CompoundTag {
        val result = CompoundTag()

        result.putString("wild_texture", value.wildTexture)
        result.putString("tame_texture", value.tameTexture)
        result.putString("angry_texture", value.angryTexture)
        result.putList("biomes", emptyList())

        return result
    }

}
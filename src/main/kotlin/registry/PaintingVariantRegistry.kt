package com.aznos.registry

import com.aznos.data.PaintingVariants
import com.aznos.datatypes.NBTType
import com.aznos.packets.newPacket.ResourceLocation
import dev.dewy.nbt.tags.collection.CompoundTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

class PaintingVariantRegistry : Registry<PaintingVariantRegistry.PaintingVariant>(ResourceLocation.vanilla("painting_variant")) {

    init {
        for (entry in PaintingVariants.entries) {
            register(ResourceLocation.vanilla(entry.name.lowercase()), PaintingVariant(
                entry.assetId,
                entry.height,
                entry.width,
                if (entry.title != null) getTranslatableComponent(entry.assetId, "title", NamedTextColor.YELLOW) else null,
                if (entry.author != null) getTranslatableComponent(entry.assetId, "author", NamedTextColor.GRAY) else null,
            ))
        }
    }

    private fun getTranslatableComponent(key: String, assetId: String, color: TextColor): CompoundTag {
        val component = Component.translatable("painting.minecraft.$assetId.$key").color(color)
        return NBTType.componentToNbtComponent(component)
    }

    override fun asCompound(value: PaintingVariant): CompoundTag {
        val result = CompoundTag()

        result.putString("asset_id", value.assetId)
        result.putInt("height", value.height)
        result.putInt("width", value.width)
        value.title?.let { result.put<CompoundTag>("title", it) }
        value.author?.let { result.put<CompoundTag>("author", it) }

        return result
    }

    data class PaintingVariant(
        val assetId: String,
        val height: Int,
        val width: Int,
        val title: CompoundTag?,
        val author: CompoundTag?
    )
}

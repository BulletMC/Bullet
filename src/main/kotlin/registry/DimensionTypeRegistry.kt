package com.aznos.registry

import com.aznos.data.DimensionTypes
import com.aznos.datatypes.putBoolean
import com.aznos.packets.newPacket.ResourceLocation
import dev.dewy.nbt.tags.collection.CompoundTag

class DimensionTypeRegistry : Registry<DimensionTypes>(ResourceLocation.vanilla("dimension_type")) {

    init {
        for (entry in DimensionTypes.entries) {
            register(ResourceLocation.vanilla(entry.name.lowercase()), entry)
        }
    }

    override fun asCompound(value: DimensionTypes): CompoundTag {
        val result = CompoundTag()

        result.putFloat("ambient_light", value.ambientLight)
        result.putBoolean("bed_works", value.bedWorks)
        result.putDouble("coordinate_scale", value.coordinateScale)
        result.putBoolean("has_ceiling", value.hasCeiling)
        result.putBoolean("has_raids", value.hasRaids)
        result.putBoolean("has_skylight", value.hasSkyLight)
        result.putInt("height", value.height)
        result.putString("infiniburn", value.infiniburn)
        result.putInt("logical_height", value.logicalHeight)
        result.putInt("min_y", value.minY)
        result.putInt("monster_spawn_light_level", 0)
        result.putInt("monster_spawn_block_light_limit", 0)
        result.putBoolean("natural", value.natural)
        result.putBoolean("piglin_safe", value.piglinSafe)
        result.putBoolean("respawn_anchor_works", value.respawnAnchorWorks)
        result.putBoolean("ultrawarm", value.ultrawarm)

        return result
    }

}
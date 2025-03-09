package com.aznos.packets.data.block.states.type

import com.aznos.packets.data.MaterialType
import com.aznos.packets.newPacket.ResourceLocation

class StateType(
    val blastResistance: Float,
    val hardness: Float,
    val isSolid: Boolean,
    val isAir: Boolean,
    val requiresCorrectTool: Boolean,
    val exceedsCube: Boolean,
    val materialType: MaterialType
) {

    val isReplacable: Boolean
        get() {
            return when (materialType) {
                MaterialType.AIR, MaterialType.STRUCTURAL_AIR,
                MaterialType.REPLACEABLE_PLANT, MaterialType.REPLACEABLE_FIREPROOF_PLANT,
                MaterialType.REPLACEABLE_WATER_PLANT, MaterialType.WATER,
                MaterialType.BUBBLE_COLUMN, MaterialType.LAVA, MaterialType.TOP_SNOW,
                MaterialType.FIRE -> true
                else -> false
            }
        }

    class Defined(
        val stateType: StateType,
        val name: ResourceLocation,
        val id: Int
    )
}
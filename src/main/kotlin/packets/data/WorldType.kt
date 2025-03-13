package com.aznos.packets.data

enum class WorldType(val named: String) {

    DEFAULT("default"),
    FLAT("flat"),
    LARGE_BIOMES("largeBiomes"),
    AMPLIFIED("amplified"),
    CUSTOMIZED("customized"),
    BUFFET("buffet"),
    DEBUG_ALL_BLOCK_STATES("debug_all_block_states"),
    DEFAULT_1_1("default_1_1");

    companion object {
        fun byName(name: String): JointType? {
            return JointType.entries.find { it.named == name }
        }
    }
}
package com.aznos.packets.data

import net.kyori.adventure.text.Component

enum class JointType(val named: String) {

    ROLLABLE("rollable"),
    ALIGNED("aligned");

    val translatableName: Component
        get() {
            return Component.translatable("jigsaw_block.joint.$named")
        }

    companion object {
        fun byName(name: String): JointType? {
            return entries.find { it.named == name }
        }
    }

}
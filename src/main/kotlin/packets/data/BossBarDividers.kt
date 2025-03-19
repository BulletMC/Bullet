package com.aznos.packets.data

/**
 * Represents how many dividers are in a boss bar
 */
enum class BossBarDividers(val id: Int) {
    NONE(0),
    NOTCHES_6(1),
    NOTCHES_10(2),
    NOTCHES_12(3),
    NOTCHES_20(4)
}
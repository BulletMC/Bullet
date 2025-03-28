package com.aznos.world.data

enum class Difficulty(val id: Int) {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3),
    ;

    companion object {
        // Note: only works as id is in the same order as difficulties order
        fun getDifficultyFromID(id: Int, default: Difficulty = NORMAL): Difficulty {
            return entries[id.takeIf { it >= 0 && it < entries.size} ?: default.id]
        }

    }

}
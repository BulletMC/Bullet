package com.maddoxh.bullet.entity.player

enum class GameMode(val id: Int) {
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);

    companion object {
        fun fromID(id: Int) = entries.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("No game mode with id $id")
    }
}
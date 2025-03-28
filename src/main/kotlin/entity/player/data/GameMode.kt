package com.aznos.entity.player.data

@Suppress("unused")
enum class GameMode(var id: Int) {
    NONE(-1),
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);
}
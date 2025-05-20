package com.aznos.world.sounds

enum class SoundCategories(val id: Int) {
    MASTER(0),
    MUSIC(1),
    RECORDS(2),
    WEATHER(3),
    BLOCKS(4),
    HOSTILE(5),
    NEUTRAL(6),
    PLAYER(7),
    AMBIENT(8),
    VOICE(9);

    companion object {
        fun fromId(id: Int): SoundCategories? {
            return entries.find { it.id == id }
        }
    }
}
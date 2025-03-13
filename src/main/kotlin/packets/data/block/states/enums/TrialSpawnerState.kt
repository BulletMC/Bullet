package com.aznos.packets.data.block.states.enums

enum class TrialSpawnerState {
    INACTIVE,
    WAITING_FOR_PLAYERS,
    ACTIVE,
    WAITING_FOR_REWARD_EJECTION,
    EJECTING_REWARD,
    COOLDOWN,
}
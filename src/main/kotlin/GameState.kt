package com.aznos

/**
 * Represents the current state of the game connection
 */
enum class GameState {
    HANDSHAKE,
    STATUS,
    LOGIN,
    PLAY,
    DISCONNECTED,
}
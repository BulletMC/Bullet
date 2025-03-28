package com.aznos.storage

import com.aznos.entity.player.Player
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.PlayerData
import java.util.*

interface AbstractServerStorage {

    /**
     * Get the world storage
     *
     * @param name The name of the world to load
     * @return A world storage instance from this server storage
     */
    fun prepareWorldStorage(name: String): AbstractWorldStorage

    /**
     * Reads player data from the storage. null if absent
     *
     * @param uuid The UUID of the player to read data for
     * @return The player data that was read from the storage or null if absent
     */
    fun readPlayerData(uuid: UUID): PlayerData?

    /**
     * Writes player data to the storage, containing information like the player's location, health, food level, etc
     *
     * @param data The player data to save
     * @return If the operation was successful
     */
    fun writePlayerData(data: PlayerData): Boolean

    /**
     * Writes player data to the disk, containing information like the player's location, health, food level, etc
     *
     * @param player The player to save its data
     * @return If the operation was successful
     */
    fun writePlayerData(player: Player): Boolean {
        return writePlayerData(
            PlayerData(
                player.username,
                player.uuid,
                player.location,
                player.status.health,
                player.status.foodLevel,
                player.status.saturation,
                player.status.exhaustion
            )
        )
    }

}
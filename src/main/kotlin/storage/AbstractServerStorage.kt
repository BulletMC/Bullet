package com.aznos.storage

import com.aznos.entity.player.Player
import com.aznos.entity.player.data.BanData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.EntityData
import com.aznos.world.data.PlayerData
import org.jetbrains.annotations.Contract
import java.util.*

interface AbstractServerStorage {

    /**
     * Get the world storage
     *
     * @param name The name of the world to load
     * @return A world storage instance from this server storage
     */
    @Contract(pure = true)
    fun prepareWorldStorage(name: String): AbstractWorldStorage

    /**
     * Reads player data from the storage. null if absent
     *
     * @param uuid The UUID of the player to read data for
     * @return The player data that was read from the storage or null if absent
     */
    @Contract(pure = true)
    fun readPlayerData(uuid: UUID): PlayerData?

    /**
     * Writes player data to the storage, containing information like the player's location, health, food level, etc
     *
     * @param data The player data to save
     * @return Whether the operation was successful or not
     */
    fun writePlayerData(data: PlayerData): Boolean

    /**
     * Writes player data to the disk, containing information like the player's location, health, food level, etc
     *
     * @param player The player to save its data
     * @return Whether the operation was successful or not
     */
    fun writePlayerData(player: Player): Boolean {
        val inventory = mutableListOf<Pair<Int, Int>>()
        for(item in player.inventory.items) {
            val slot = item.key
            val id = item.value

            inventory.add(Pair(slot, id))
        }

        return writePlayerData(
            PlayerData(
                player.username,
                player.uuid,
                player.location,
                player.status.health,
                player.status.foodLevel,
                player.status.saturation,
                player.status.exhaustion,
                inventory,
                player.permissionLevel
            )
        )
    }

    /**
     * Read player ban list from the storage
     *
     * @return A set containing all the id of currently banned player
     */
    @Contract(pure = true)
    fun readBannedList(): Collection<BanData>

    /**
     * Write player ban list to the storage
     *
     * @param banned A set of every banned players
     * @return Whether the operation was successful or not
     */
    fun writeBannedList(banned: Collection<BanData>): Boolean
}
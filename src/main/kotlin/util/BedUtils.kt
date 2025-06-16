package com.aznos.util

import com.aznos.Bullet
import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.MetadataType
import com.aznos.packets.play.out.ServerChangeGameStatePacket
import com.aznos.packets.play.out.ServerEntityMetadataPacket
import com.aznos.world.World
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.ranges.contains
import kotlin.time.Duration.Companion.seconds

object BedUtils {
    /**
     * Handles the click on a bed block
     *
     * @param client The client session of the player clicking the bed.
     * @param blockPos The position of the bed block being clicked.
     */
    fun handleBedClick(client: ClientSession, blockPos: BlockPositionType.BlockPosition) {
        val world = client.player.world!!
        val time = world.timeOfDay

        if(world.weather == 0) {
            if(time !in 12542..23459) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )

                return
            }
        } else {
            if(time !in 12010..23991) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )
                return
            }
        }

        val metadata = listOf(
            MetadataType.MetadataEntry(6.toByte(), 18, 2),
            MetadataType.MetadataEntry(13.toByte(), 10, true to blockPos),
        )

        for (player in players) {
            player.sendPacket(ServerEntityMetadataPacket(client.player.entityID, metadata))
        }

        world.sleepingPlayers += 1
        if(canSleepNow(world)) handleSleeping(client)
    }

    /**
     * Handles the sleeping action for a player.
     * This method is called when a player is ready to sleep in a bed.
     *
     * @param client The client session of the player who is sleeping.
     */
    fun handleSleeping(client: ClientSession) {
        Bullet.scope.launch {
            delay(5.seconds)
            val world = client.player.world!!
            if(!canSleepNow(world)) return@launch

            for(player in players) {
                player.sendPacket(ServerChangeGameStatePacket(1, 0f))
                world.weather = 0
                player.setTimeOfDay(0)
                handleWakeUp(world, client)
            }
        }
    }

    /**
     * Checks if the conditions are met for players to sleep in the world.
     *
     * @param world The world in which the players are trying to sleep.
     * @return True if the conditions for sleeping are met, false otherwise.
     */
    fun canSleepNow(world: World): Boolean {
        val time = world.timeOfDay
        val totalPlayers = players.size
        val sleepingPlayers = world.sleepingPlayers

        return if (totalPlayers > 0 && sleepingPlayers >= totalPlayers / 2) {
            if(world.weather == 0) {
                time in 12542..23459
            } else {
                time in 12010..23991
            }
        } else {
            false
        }
    }

    /**
     * Handles the wake-up process for players after sleeping.
     *
     * @param world The world in which the players are waking up.
     * @param client The client session of the player waking up.
     */
    fun handleWakeUp(world: World, client: ClientSession) {
        if(!canSleepNow(world)) return

        val metadata = listOf(
            MetadataType.MetadataEntry(13.toByte(), 10, false to null),
            MetadataType.MetadataEntry(6.toByte(), 18, 0)
        )

        for(plr in players) {
            plr.sendPacket(ServerEntityMetadataPacket(client.player.entityID, metadata))
        }

        world.sleepingPlayers -= 1
    }
}
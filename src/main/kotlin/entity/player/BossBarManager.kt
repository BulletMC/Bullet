package com.aznos.entity.player

import com.aznos.packets.data.BossBarColor
import com.aznos.packets.data.BossBarDividers
import com.aznos.packets.play.out.ServerBossBarPacket
import java.util.*
import kotlin.experimental.or

class BossBarManager(private val player: Player) {
    val bossBars = mutableListOf<UUID>()

    /**
     * Used to send a boss bar to the player
     *
     * @param title The title of the boss bar
     * @param health The health of the boss bar, 0-1. It's possible to go over 1
     * and around 1.5 it'll start to show a second boss bar
     * @param color The color of the boss bar
     * @param dividers How many dividers should show on the boss bar
     * @param darkenSky If the boss bar should darken the sky
     * @param playEndMusic If the boss bar should play end music (Used for the ender dragon)
     * @param createFog If the boss bar should create fog
     */
    fun addBossBar(
        title: String,
        health: Float? = 1f,
        color: BossBarColor? = BossBarColor.PINK,
        dividers: BossBarDividers? = BossBarDividers.NONE,
        darkenSky: Boolean = false,
        playEndMusic: Boolean = false,
        createFog: Boolean = false
    ) {
        val uuid = UUID.randomUUID()
        bossBars.add(uuid)

        var flags: Byte = 0
        if(darkenSky) flags = flags or 0x1
        if(playEndMusic) flags = flags or 0x2
        if(createFog) flags = flags or 0x04

        player.sendPacket(
            ServerBossBarPacket(
            uuid,
            ServerBossBarPacket.Action.ADD,
            title,
            health,
            color,
            dividers,
            flags
        )
        )
    }

    /**
     * Updates an existing boss bar with new information
     *
     * @param id The ID of the boss bar to update, this is the index in the [bossBars]
     * @param action The action to perform on the boss bar
     * @param title The new title of the boss bar
     * @param health The new health of the boss bar
     * @param color The new color of the boss bar
     * @param dividers The new amount of dividers for the boss bar
     * @param darkenSky If the boss bar should darken the sky
     * @param playEndMusic If the boss bar should play end music (Used for the ender dragon)
     * @param createFog If the boss bar should create fog
     */
    fun updateBossBar(
        id: Int,
        action: ServerBossBarPacket.Action,
        title: String? = null,
        health: Float? = null,
        color: BossBarColor? = null,
        dividers: BossBarDividers? = null,
        darkenSky: Boolean? = null,
        playEndMusic: Boolean? = null,
        createFog: Boolean? = null
    ) {
        val uuid = bossBars[id]

        when(action) {
            ServerBossBarPacket.Action.ADD -> {

            }

            ServerBossBarPacket.Action.REMOVE -> {
                bossBars.removeAt(id)
                player.sendPacket(ServerBossBarPacket(uuid, action))
            }

            ServerBossBarPacket.Action.UPDATE_HEALTH -> {
                player.sendPacket(ServerBossBarPacket(uuid, action, health = health))
            }

            ServerBossBarPacket.Action.UPDATE_TITLE -> {
                player.sendPacket(ServerBossBarPacket(uuid, action, title = title))
            }

            ServerBossBarPacket.Action.UPDATE_STYLE -> {
                player.sendPacket(ServerBossBarPacket(uuid, action, color = color, division = dividers))
            }

            ServerBossBarPacket.Action.UPDATE_FLAGS -> {
                var flags: Byte = 0
                if(darkenSky == true) flags = flags or 0x1
                if(playEndMusic == true) flags = flags or 0x2
                if(createFog == true) flags = flags or 0x04

                player.sendPacket(ServerBossBarPacket(uuid, action, flags = flags))
            }
        }
    }
}
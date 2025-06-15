package com.aznos.util

import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.entity.Entity
import com.aznos.packets.play.out.ServerCollectItemPacket
import com.aznos.packets.play.out.ServerDestroyEntitiesPacket
import com.aznos.packets.play.out.ServerSetExperiencePacket
import com.aznos.packets.play.out.ServerSoundEffectPacket
import com.aznos.world.World
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds
import kotlin.math.pow
import kotlin.math.sqrt

object OrbUtils {
    fun checkOrbs(world: World, client: ClientSession) {
        for(player in players) {
            val toRemove = mutableListOf<Entity>()
            for(orb in world.orbs) {
                val distance = sqrt(
                    (player.location.x - orb.location.x).pow(2) +
                            (player.location.y - orb.location.y).pow(2) +
                            (player.location.z - orb.location.z).pow(2)
                )

                if(distance <= 1.25) {
                    client.player.sendPacket(
                        ServerCollectItemPacket(
                            orb.entityID,
                            client.player.entityID,
                            1
                        )
                    )

                    client.player.sendPacket(ServerDestroyEntitiesPacket(intArrayOf(orb.entityID)))
                    client.player.sendPacket(
                        ServerSoundEffectPacket(
                            Sounds.ENTITY_EXPERIENCE_ORB_PICKUP,
                            SoundCategories.PLAYER,
                            client.player.location.x.toInt(),
                            client.player.location.y.toInt(),
                            client.player.location.z.toInt()
                        )
                    )

                    calculateXPLevels(client.player.totalXP + orb.xp, client)
                    toRemove.add(orb)
                }
            }

            world.orbs.removeAll(toRemove)
        }
    }

    fun xpToNextLevel(level: Int): Int = when {
        level < 16 -> 2 * level + 7
        level < 31 -> 5 * level - 38
        else -> 9 * level - 158
    }

    fun totalXPTillNextLevel(level: Int): Int = when {
        level <= 16 -> level * level + 6 * level
        level <= 31 -> (2.5 * level * level - 40.5 * level + 360).toInt()
        else -> (4.5 * level * level - 162.5 * level + 2220).toInt()
    }

    fun calculateXPLevels(totalXP: Int, client: ClientSession) {
        val player = client.player
        player.totalXP = totalXP

        var level = 0
        while(totalXPTillNextLevel(level + 1) <= player.totalXP) {
            level++
        }

        val xpIntoLevel = player.totalXP - totalXPTillNextLevel(level)
        val xpNeeded = xpToNextLevel(level).toFloat()

        player.level = level
        player.experienceBar = if (xpNeeded == 0f) 0f else xpIntoLevel / xpNeeded

        player.sendPacket(ServerSetExperiencePacket(player.experienceBar, player.level, player.totalXP))
    }
}
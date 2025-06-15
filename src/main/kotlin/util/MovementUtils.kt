package com.aznos.util

import com.aznos.Bullet.sprinting
import com.aznos.ClientSession
import com.aznos.datatypes.LocationType
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.EventManager
import com.aznos.events.PlayerMoveEvent
import com.aznos.packets.play.out.ServerUpdateHealthPacket
import com.aznos.packets.play.out.ServerUpdateViewPositionPacket
import kotlin.math.pow
import kotlin.math.sqrt

object MovementUtils {
    fun calculateDeltas(
        currentX: Double, currentY: Double, currentZ: Double,
        lastX: Double, lastY: Double, lastZ: Double
    ): Triple<Short, Short, Short> {
        val deltaX = ((currentX - lastX) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        val deltaY = ((currentY - lastY) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        val deltaZ = ((currentZ - lastZ) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        return Triple(deltaX, deltaY, deltaZ)
    }

    fun handleMove(
        client: ClientSession,
        newLocation: LocationType.Location,
        onGround: Boolean,
    ): Boolean {
        val player = client.player
        val event = PlayerMoveEvent(
            player,
            newLocation,
            player.location.copy()
        )
        EventManager.fire(event)
        if(event.isCancelled) return false

        val wasOnGround = player.onGround

        val newChunkX = (newLocation.x / 16).toInt()
        val newChunkZ = (newLocation.z / 16).toInt()

        if(newChunkX != player.chunkX || newChunkZ != player.chunkZ) {
            player.chunkX = newChunkX
            player.chunkZ = newChunkZ

            client.sendPacket(
                ServerUpdateViewPositionPacket(
                    newChunkX,
                    newChunkZ
                )
            )
            client.updatePlayerChunks(newChunkX, newChunkZ)
        }

        handleFoodLevel(player, newLocation.x, newLocation.z, onGround, wasOnGround)

        player.location = newLocation
        player.onGround = onGround
        checkFallDamage(client)
        OrbUtils.checkOrbs(client.player.world!!, client)
        ItemUtils.checkItems(client, client.player.world!!)

        return true
    }

    private fun handleFoodLevel(player: Player, x: Double, z: Double, onGround: Boolean, wasOnGround: Boolean) {
        if (!onGround && wasOnGround) {
            if (sprinting.contains(player.entityID)) {
                player.status.exhaustion += 0.2f
            } else {
                player.status.exhaustion += 0.05f
            }
        }

        if (sprinting.contains(player.entityID)) {
            val distance = sqrt(
                (x - player.lastSprintLocation!!.x).pow(2) +
                        (z - player.lastSprintLocation!!.z).pow(2)
            )

            if (distance >= 1) {
                player.status.exhaustion += 0.1f
                player.lastSprintLocation = player.location
            }
        }
    }

    private fun checkFallDamage(client: ClientSession) {
        val player = client.player
        if (player.gameMode == GameMode.SURVIVAL) {
            if (player.onGround) {
                if (player.fallDistance > 3) {
                    val damage = ((player.fallDistance - 3).coerceAtLeast(0.0)).toInt()
                    player.status.health -= damage

                    player.sendPacket(
                        ServerUpdateHealthPacket(
                            player.status.health.toFloat(),
                            player.status.foodLevel,
                            player.status.saturation
                        )
                    )
                }

                player.fallDistance = 0.0
                player.lastOnGroundY = player.location.y
            } else {
                if (player.location.y < player.lastOnGroundY) {
                    player.fallDistance += player.lastOnGroundY - player.location.y
                    player.lastOnGroundY = player.location.y
                } else {
                    player.lastOnGroundY = player.location.y
                }
            }
        }
    }
}
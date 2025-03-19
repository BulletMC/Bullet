package com.aznos

import com.aznos.entity.Entity
import com.aznos.entity.nonliving.Entities
import com.aznos.events.EventManager
import com.aznos.events.PlayerSneakEvent
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import kotlin.math.cos
import kotlin.math.sin

/**
 * Application entry point
 */
fun main() {
    EventManager.register(PlayerSneakEvent::class.java) { e ->
        if(e.isSneaking) {
            val entity = Entity()
            val player = e.player
            val yaw = player.location.yaw.toDouble()
            val pitch = player.location.pitch.toDouble()

            val directionX = -sin(yaw / 180.0 * Math.PI) * cos(pitch / 180.0 * Math.PI) * 2
            val directionY = -sin(pitch / 180.0 * Math.PI) * 2
            val directionZ = cos(yaw / 180.0 * Math.PI) * cos(pitch / 180.0 * Math.PI) * 2

            for(player in Bullet.players) {
                player.sendPacket(ServerSpawnEntityPacket(
                    entity.entityID,
                    entity.uuid,
                    Entities.FIREBALL.id,
                    player.location,
                    directionX.toInt().toShort(),
                    directionY.toInt().toShort(),
                    directionZ.toInt().toShort(),
                    0
                ))
            }
        }
    }

    Bullet.createServer("0.0.0.0")
}
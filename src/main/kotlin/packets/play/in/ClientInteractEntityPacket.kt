package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.GameMode
import com.aznos.events.EventManager
import com.aznos.events.PlayerInteractEntityEvent
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerAnimationPacket
import com.aznos.packets.play.out.ServerUpdateHealthPacket
import com.aznos.packets.play.out.movement.ServerEntityVelocityPacket
import kotlin.math.sqrt

/**
 * This packet is sent from the client to the server when the client attacks
 * or right clicks another entity
 *
 * The server only accepts this packet if the entity being attached/used is
 * visible without obstruction and within a 4 unit radius of the players position
 *
 * @property entityID The ID of the entity being interacted with
 * @property type The type of interaction, 0: Interact, 1: Attack, 2: Interact At
 * @property targetX Only if type is interact at
 * @property targetY Only if type is interact at
 * @property targetZ Only if type is interact at
 * @property hand Only if type is interact or interact at, 0: Main hand, 1: Off-hand
 * @property sneaking If the player is sneaking
 */
class ClientInteractEntityPacket(data: ByteArray) : Packet(data) {
    val entityID: Int
    val type: Int
    val targetX: Float?
    val targetY: Float?
    val targetZ: Float?
    val hand: Int?
    val sneaking: Boolean

    init {
        val input = getIStream()

        entityID = input.readVarInt()
        type = input.readVarInt()

        if(type == 2) {
            targetX = input.readFloat()
            targetY = input.readFloat()
            targetZ = input.readFloat()
        } else {
            targetX = null
            targetY = null
            targetZ = null
        }

        hand = if(type == 0 || type == 2) {
            input.readVarInt()
        } else {
            null
        }

        sneaking = input.readBoolean()
    }

    override fun apply(client: ClientSession) {
        val attacker = client.player

        val event = PlayerInteractEntityEvent(attacker, entityID, type)
        EventManager.fire(event)
        if(event.isCancelled) return

        if(type == 1) {
            for(player in Bullet.players) {
                if(player.entityID == entityID && player.gameMode == GameMode.SURVIVAL) {
                    player.status.health -= 1

                    player.sendPacket(
                        ServerUpdateHealthPacket(
                            player.status.health.toFloat(),
                            player.status.foodLevel,
                            player.status.saturation
                        )
                    )

                    player.sendPacket(
                        ServerAnimationPacket(
                            player.entityID,
                            1
                        )
                    )

                    player.status.exhaustion += 0.1f

                    val dx = player.location.x - attacker.location.x
                    val dy = player.location.y - attacker.location.y
                    val dz = player.location.z - attacker.location.z
                    val distance = sqrt(dx * dx + dy * dy + dz * dz)
                    if(distance != 0.0) {
                        val kbStrength = 0.5

                        val kbX = (dx / distance) * kbStrength
                        val kbY = if (player.onGround) 0.3 else 0.125
                        val kbZ = (dz / distance) * kbStrength

                        player.sendPacket(
                            ServerEntityVelocityPacket(
                                player.entityID,
                                (kbX * 8000).toInt().toShort(),
                                (kbY * 8000).toInt().toShort(),
                                (kbZ * 8000).toInt().toShort()
                            )
                        )
                    }
                }
            }
        }
    }
}
package com.aznos.packets.play.`in`.movement

import com.aznos.Bullet
import com.aznos.Bullet.sprinting
import com.aznos.ClientSession
import com.aznos.datatypes.MetadataType
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.Player
import com.aznos.events.EventManager
import com.aznos.events.PlayerSneakEvent
import com.aznos.events.PlayerSprintEvent
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerEntityMetadataPacket
import com.aznos.util.BedUtils

/**
 * This packet is sent to indicate that the client has performed certain actions
 * including sneaking, sprinting, jumping with a horse, etc
 *
 * Action IDs:
 * 0 - Start sneaking
 * 1 - Stop sneaking
 * 2 - Leave bed
 * 3 - Start sprinting
 * 4 - Stop sprinting
 * 5 - Start jump with horse
 * 6 - Stop jump with horse
 * 7 - Open horse inventory
 * 8 - Start flying with elytra
 *
 * Leave bed is only sent when the "Leave bed" button is clicked,
 * not when waking up naturally
 *
 * @property entityID The ID of the entity performing the action
 * @property actionID The ID of the action, see above
 * @property jumpBoost Only used by the "start jump with horse" action, 0-100
 */
class ClientEntityActionPacket(data: ByteArray) : Packet(data) {
    val entityID: Int
    val actionID: Int
    var jumpBoost: Int = 0

    init {
        val input = getIStream()

        entityID = input.readVarInt()
        actionID = input.readVarInt()
        jumpBoost = input.readVarInt()
    }

    override fun apply(client: ClientSession) {
        when(actionID) {
            0 -> { //Start sneaking
                val event = PlayerSneakEvent(client.player, true)
                EventManager.fire(event)
                if (event.isCancelled) return

                client.player.isSneaking = true
                updateEntityMetadata(client.player, 6, 5)
            }

            1 -> { //Stop sneaking
                val event = PlayerSneakEvent(client.player, false)
                EventManager.fire(event)
                if (event.isCancelled) return

                client.player.isSneaking = false
                updateEntityMetadata(client.player, 6, 0)
            }

            2 -> { //Leave bed
                BedUtils.handleWakeUp(client.player.world!!, client)
            }

            3 -> { //Start sprinting
                val event = PlayerSprintEvent(client.player, true)
                EventManager.fire(event)
                if(event.isCancelled) return

                sprinting.add(client.player.entityID)
                client.player.lastSprintLocation = client.player.location
            }

            4 -> { //Stop sprinting
                val event = PlayerSprintEvent(client.player, false)
                EventManager.fire(event)
                if(event.isCancelled) return

                sprinting.remove(client.player.entityID)
                client.player.lastSprintLocation = null
            }
        }
    }

    private fun updateEntityMetadata(player: Player, index: Int, value: Int) {
        val packet = ServerEntityMetadataPacket(
            player.entityID,
            listOf(MetadataType.MetadataEntry(index.toByte(), 18, value))
        )

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != player) {
                otherPlayer.sendPacket(packet)
            }
        }
    }
}
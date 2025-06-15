package com.aznos.packets.play.`in`.movement

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.events.EventManager
import com.aznos.events.PlayerMoveEvent
import com.aznos.packets.Packet
import com.aznos.packets.play.out.movement.ServerEntityHeadLook
import com.aznos.packets.play.out.movement.ServerEntityRotationPacket

/**
 * This packet is by the client whenever the players rotation is updated
 */
class ClientPlayerRotation(data: ByteArray) : Packet(data) {
    val yaw: Float
    val pitch: Float
    val onGround: Boolean

    init {
        val input = getIStream()

        yaw = input.readFloat()
        pitch = input.readFloat()
        onGround = input.readBoolean()
    }

    override fun apply(client: ClientSession) {
        val newLocation = client.player.location.set(yaw, pitch)
        val event = PlayerMoveEvent(
            client.player,
            newLocation,
            client.player.location.copy()
        )

        EventManager.fire(event)
        if(event.isCancelled) return

        val player = client.player
        player.location = newLocation
        player.onGround = onGround

        val rotPacket = ServerEntityRotationPacket(
            player.entityID,
            player.location.yaw,
            player.location.pitch,
            player.onGround
        )

        val headLookPacket = ServerEntityHeadLook(
            player.entityID,
            player.location.yaw
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(rotPacket)
            otherPlayer.clientSession.sendPacket(headLookPacket)
        }
    }
}
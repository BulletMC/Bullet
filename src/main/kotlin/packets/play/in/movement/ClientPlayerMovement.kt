package com.aznos.packets.play.`in`.movement

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.packets.Packet
import com.aznos.packets.play.out.movement.ServerEntityMovementPacket

/**
 * This packet is by the client every 20 ticks from stationary players
 */
class ClientPlayerMovement(data: ByteArray) : Packet(data) {
    val onGround: Boolean = getIStream().readBoolean()

    override fun apply(client: ClientSession) {
        val player = client.player
        player.onGround = onGround

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != player) continue
            otherPlayer.clientSession.sendPacket(ServerEntityMovementPacket(player.entityID))
        }
    }
}
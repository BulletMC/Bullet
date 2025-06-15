package com.aznos.packets.play.`in`.movement

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.packets.Packet
import com.aznos.packets.play.out.movement.ServerEntityPositionPacket
import com.aznos.util.MovementUtils

/**
 * This packet is by the client whenever the players position is updated
 */
class ClientPlayerPositionPacket(data: ByteArray) : Packet(data) {
    val x: Double
    val feetY: Double
    val z: Double
    val onGround: Boolean

    init {
        val input = getIStream()

        x = input.readDouble()
        feetY = input.readDouble()
        z = input.readDouble()
        onGround = input.readBoolean()
    }

    override fun apply(client: ClientSession) {
        val newLocation = client.player.location.set(x, feetY, z)
        val player = client.player
        val lastLocation = player.location

        if(!MovementUtils.handleMove(client, newLocation, onGround)) return

        val (deltaX, deltaY, deltaZ) = MovementUtils.calculateDeltas(
            x, feetY, z,
            lastLocation.x, lastLocation.y, lastLocation.z
        )

        val posPacket = ServerEntityPositionPacket(
            player.entityID,
            deltaX,
            deltaY,
            deltaZ,
            player.onGround
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue
            otherPlayer.clientSession.sendPacket(posPacket)
        }
    }
}
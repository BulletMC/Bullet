package com.aznos.packets.play.`in`.movement

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.LocationType
import com.aznos.packets.Packet
import com.aznos.packets.play.out.movement.ServerEntityHeadLook
import com.aznos.packets.play.out.movement.ServerEntityPositionAndRotationPacket
import com.aznos.util.MovementUtils

/**
 * This packet is by the client whenever the players position and rotation is updated
 */
class ClientPlayerPositionAndRotation(data: ByteArray) : Packet(data) {
    val x: Double
    val feetY: Double
    val z: Double
    val yaw: Float
    val pitch: Float
    val onGround: Boolean

    init {
        val input = getIStream()

        x = input.readDouble()
        feetY = input.readDouble()
        z = input.readDouble()
        yaw = input.readFloat()
        pitch = input.readFloat()
        onGround = input.readBoolean()
    }

    override fun apply(client: ClientSession) {
        val newLocation = LocationType.Location(x, feetY, z, yaw, pitch)

        val player = client.player
        val lastLocation = player.location

        if(!MovementUtils.handleMove(client, newLocation, onGround)) return

        val(deltaX, deltaY, deltaZ) = MovementUtils.calculateDeltas(
            x, feetY, z,
            lastLocation.x, lastLocation.y, lastLocation.z
        )

        val posAndRotPacket = ServerEntityPositionAndRotationPacket(
            player.entityID,
            deltaX,
            deltaY,
            deltaZ,
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

            otherPlayer.clientSession.sendPacket(posAndRotPacket)
            otherPlayer.clientSession.sendPacket(headLookPacket)
        }
    }
}
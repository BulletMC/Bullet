package com.aznos.packets.play.out

import com.aznos.packets.Packet

/**
 * Sends a particle effect to the client
 */
class ServerParticlePacket(
    particleID: Int,
    longDistance: Boolean,
    x: Double,
    y: Double,
    z: Double,
    offsetX: Float,
    offsetY: Float,
    offsetZ: Float,
    particleData: Float,
    particleCount: Int,
    data: Any? = null
) : Packet(0x22) {
    init {

    }
}
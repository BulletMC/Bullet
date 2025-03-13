package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet
import com.aznos.world.data.Particle

/**
 * Sends a particle effect to the client
 *
 * @param particle The particle ID to send
 * @param longDistance If true, particle distance increases from 256 to 65536
 * @param position The position of the particle
 * @param offsetX The X offset of the particle (multiplied by nextGaussian)
 * @param offsetY The Y offset of the particle (multiplied by nextGaussian)
 * @param offsetZ The Z offset of the particle (multiplied by nextGaussian)
 * @param particleData The data of each particle
 * @param particleCount The amount of particles to spawn
 */
class ServerParticlePacket(
    particle: Particle,
    longDistance: Boolean,
    position: Position,
    offsetX: Float,
    offsetY: Float,
    offsetZ: Float,
    particleData: Float,
    particleCount: Int
) : Packet(0x22) {
    init {
        wrapper.writeInt(particle.id)
        wrapper.writeBoolean(longDistance)
        wrapper.writeDouble(position.x)
        wrapper.writeDouble(position.y)
        wrapper.writeDouble(position.z)
        wrapper.writeFloat(offsetX)
        wrapper.writeFloat(offsetY)
        wrapper.writeFloat(offsetZ)
        wrapper.writeFloat(particleData)
        wrapper.writeInt(particleCount)

        when(particle) {
            is Particle.Block -> {
                wrapper.writeVarInt(particle.blockState)
            }
            is Particle.Dust -> {
                wrapper.writeFloat(particle.red)
                wrapper.writeFloat(particle.green)
                wrapper.writeFloat(particle.blue)
                wrapper.writeFloat(particle.scale)
            }
            is Particle.FallingDust -> {
                wrapper.writeVarInt(particle.blockState)
            }
            is Particle.Item -> {
                wrapper.writeVarInt(particle.itemId)
            }
            else -> {}
        }
    }
}
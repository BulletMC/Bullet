package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeVanillaBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.world.data.Particles

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
    particle: Particles,
    longDistance: Boolean,
    position: BlockPositionType.BlockPosition,
    offsetX: Float,
    offsetY: Float,
    offsetZ: Float,
    particleData: Float,
    particleCount: Int
) : Packet(0x22) {
    init {
        wrapper.writeInt(particle.id)
        wrapper.writeBoolean(longDistance)
        wrapper.writeVanillaBlockPos(position)
        wrapper.writeFloat(offsetX)
        wrapper.writeFloat(offsetY)
        wrapper.writeFloat(offsetZ)
        wrapper.writeFloat(particleData)
        wrapper.writeInt(particleCount)

        when(particle) {
            is Particles.Block -> {
                wrapper.writeVarInt(particle.blockState)
            }
            is Particles.Dust -> {
                wrapper.writeFloat(particle.red)
                wrapper.writeFloat(particle.green)
                wrapper.writeFloat(particle.blue)
                wrapper.writeFloat(particle.scale)
            }
            is Particles.FallingDust -> {
                wrapper.writeVarInt(particle.blockState)
            }
            is Particles.Item -> {
                wrapper.writeVarInt(particle.itemId)
            }
            else -> {}
        }
    }
}
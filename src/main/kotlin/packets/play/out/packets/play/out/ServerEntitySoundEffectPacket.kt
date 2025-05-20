package com.aznos.packets.play.out.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds

/**
 * This packet is used to play a sound effect from an entity
 *
 * @param soundID ID of hardcoded sound event
 * @param soundCategory The category that this sound will be played from
 * @param entityID Which entity to play the sound from
 * @param volume Capped between 0.0f and 1.0f
 * @param pitch Float between 0.5 and 2.0
 */
class ServerEntitySoundEffectPacket(
    soundID: Sounds,
    soundCategory: SoundCategories,
    entityID: Int,
    volume: Float = 1.0f,
    pitch: Float = 1.0f,
) : Packet(0x50) {
    init {
        wrapper.writeVarInt(soundID.id)
        wrapper.writeVarInt(soundCategory.id)
        wrapper.writeVarInt(entityID)
        wrapper.writeFloat(volume.coerceIn(0.0f, 1.0f))
        wrapper.writeFloat(pitch.coerceIn(0.5f, 2.0f))
    }
}
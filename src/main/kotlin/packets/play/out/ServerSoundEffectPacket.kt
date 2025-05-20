package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds

/**
 * This packet is used to play a number of hardcoded sound events
 *
 * @param soundID ID of hardcoded sound event
 * @param soundCategory The category that this sound will be played from
 * @param effectPosX Effect X multiplied by 8 with only 3 bits dedicated to the fractional part
 * @param effectPosY Effect Y multiplied by 8 with only 3 bits dedicated to the fractional part
 * @param effectPosZ Effect Z multiplied by 8 with only 3 bits dedicated to the fractional part
 * @param volume Capped between 0.0f and 1.0f
 * @param pitch Float between 0.5 and 2.0
 */
class ServerSoundEffectPacket(
    soundID: Sounds,
    soundCategory: SoundCategories,
    effectPosX: Int,
    effectPosY: Int,
    effectPosZ: Int,
    volume: Float = 1.0f,
    pitch: Float = 1.0f,
) : Packet(0x51) {
    init {
        wrapper.writeVarInt(soundID.id)
        wrapper.writeVarInt(soundCategory.id)
        wrapper.writeInt(effectPosX)
        wrapper.writeInt(effectPosY)
        wrapper.writeInt(effectPosZ)
        wrapper.writeFloat(volume.coerceIn(0.0f, 1.0f))
        wrapper.writeFloat(pitch.coerceIn(0.5f, 2.0f))
    }
}
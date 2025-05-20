package com.aznos.packets.play.out.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This packet is used to stop any playing sounds
 *
 * @param flags Controls which fields are present
 * @param source Only if flags are 3 or 1 (bit mask 0x1), if not present, then sounds from all sources are cleared
 * @param sound Only if flags is 2 or 3 (bit mask 0x2). A sound effect name, if not present, all sounds are cleared
 */
class ServerStopSoundPacket(
    flags: Byte,
    source: Int? = null,
    sound: String? = null
) : Packet(0x52) {
    init {
        wrapper.writeByte(flags.toInt())
        if(source != null) wrapper.writeVarInt(source)
        if(sound != null) wrapper.writeString(sound)
    }
}
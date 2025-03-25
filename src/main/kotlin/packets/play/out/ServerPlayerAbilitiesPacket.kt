package com.aznos.packets.play.out

import com.aznos.packets.Packet

/**
 * The latter two floats are used to indicate the field of view
 * and flying speed respectively, while the first byte is used to determine
 * the value of 4 booleans
 *
 * Available flags:
 * 0x01 - Is invulnerable
 * 0x02 - Is flying
 * 0x04 - Allow flying
 * 0x08 - Creative mode (instant block breaking)
 *
 * @param flags The flags, see above
 * @param flyingSpeed The new flying speed of the player, default is 0.05
 * @param fov Modifies the field of view, like a speed potion, default is 0.1
 */
class ServerPlayerAbilitiesPacket(
    flags: Byte,
    flyingSpeed: Float = 0.05f,
    fov: Float = 0.1f
) : Packet(0x30) {
    init {
        wrapper.writeByte(flags.toInt())
        wrapper.writeFloat(flyingSpeed)
        wrapper.writeFloat(fov)
    }
}
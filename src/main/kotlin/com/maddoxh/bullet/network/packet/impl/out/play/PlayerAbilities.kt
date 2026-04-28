package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket

data class PlayerAbilities( // 0x38 S->C
    val invulnerable: Boolean = false,
    val flying: Boolean = false,
    val allowFlying: Boolean = true,
    val instantBreak: Boolean = true,
    val flyingSpeed: Float = 0.05f,
    val walkingSpeed: Float = 0.1f
) : PlayOutboundPacket {
    override val packetId = 0x38

    private val flags: Int get() {
        var f = 0
        if(invulnerable) f = f or 0x01
        if(flying)       f = f or 0x02
        if(allowFlying)  f = f or 0x04
        if(instantBreak) f = f or 0x08
        return f
    }

    override fun encode() = MinecraftOutputStream.build {
        writeByte(flags)
        writeFloat(flyingSpeed)
        writeFloat(walkingSpeed)
    }
}

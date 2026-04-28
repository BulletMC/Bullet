package com.maddoxh.bullet.network.packet.impl.out.play

import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.out.PlayOutboundPacket
import java.util.concurrent.atomic.AtomicInteger

data class SyncPlayerPosition( // 0x40 S->C
    var x: Double = 0.0,
    val y: Double = 64.0,
    val z: Double = 0.0,
    val velocityX: Double = 0.0,
    val velocityY: Double = 0.0,
    val velocityZ: Double = 0.0,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f,
    val teleportID: Int = nextTeleportID()
) : PlayOutboundPacket {
    override val packetId = 0x40
    override fun encode() = MinecraftOutputStream.build {
        writeDouble(x)
        writeDouble(y)
        writeDouble(z)
        writeDouble(velocityX)
        writeDouble(velocityY)
        writeFloat(yaw)
        writeFloat(pitch)
        writeVarInt(teleportID)
    }

    companion object {
        private val counter = AtomicInteger(0)
        fun nextTeleportID() = counter.incrementAndGet()
    }
}

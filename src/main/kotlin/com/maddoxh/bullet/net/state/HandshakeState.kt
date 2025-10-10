package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.net.ClientSession
import com.maddoxh.bullet.packets.handshake.Handshake
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

@Suppress("UnusedPrivateProperty")
class HandshakeState(private val session: ClientSession): SessionState {
    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        if(packetId != Handshake.PACKET_ID) {
            error("Unexpected packet ID $packetId in HandshakeState")
        }

        val handshake = Handshake.readFrom(input)
        Bullet.logger.info("Received handshake: $handshake")
    }
}
package com.maddoxh.bullet.net

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.TypeHelpers
import com.maddoxh.bullet.net.state.HandshakeState
import com.maddoxh.bullet.net.state.SessionState
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.readFully
import java.lang.Exception

class ClientSession(private val socket: Socket) {
    private val input = socket.openReadChannel()
    private val output = socket.openWriteChannel(true)
    private var state: SessionState = HandshakeState(this)

    suspend fun run() {
        Bullet.logger.info("Session started with ${socket.remoteAddress}")

        try {
            while(true) {
                val frameLength = TypeHelpers.readVarInt(input)
                val frameBytes = ByteArray(frameLength)
                input.readFully(frameBytes)

                val frame = ByteReadPacket(frameBytes)
                val packetId = TypeHelpers.readVarInt(frame)

                Bullet.logger.info(
                    "Received packet with ID $packetId and length $frameLength from ${socket.remoteAddress}"
                )

                state.handleIncoming(packetId, frame, output)
            }
        } catch(e: Exception) {
            Bullet.logger.warn("Session with ${socket.remoteAddress} ended: ${e.message}")
        } finally {
            socket.close()
        }
    }
}
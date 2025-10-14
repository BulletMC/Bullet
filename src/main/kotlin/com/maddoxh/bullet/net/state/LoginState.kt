package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.net.ClientSession
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

class LoginState(private val client: ClientSession): SessionState {
    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        when(packetId) {
            else -> error("Unknown packet ID $packetId in login state")
        }
    }
}
package com.maddoxh.bullet.net.state

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

interface SessionState {
    suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel)
}
package com.maddoxh.bullet.net

import com.maddoxh.bullet.types.VarInt.writeVarInt
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PacketSender {
    suspend fun sendPacket(output: ByteWriteChannel, packetID: Int, buildPayload: BytePacketBuilder.() -> Unit) {
        val packetBuf = buildPacket {
            writeVarInt(packetID)
            buildPayload()
        }

        withContext(Dispatchers.IO) {
            val payloadBytes = packetBuf.readBytes()
            val frame = buildPacket {
                writeVarInt(payloadBytes.size)
                writeFully(payloadBytes)
            }

            output.writePacket(frame)
        }
    }
}
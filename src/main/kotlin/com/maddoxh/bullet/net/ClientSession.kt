package com.maddoxh.bullet.net

import com.maddoxh.bullet.Bullet
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.readFully
import java.lang.Exception

class ClientSession(private val socket: Socket) {
    private val input = socket.openReadChannel()
    @Suppress("unused")
    private val output = socket.openWriteChannel(true)

    suspend fun run() {
        Bullet.logger.info("Session started with ${socket.remoteAddress}")

        try {
            while(true) {
                val length = readVarInt()
                val packetId = readVarInt()
                val data = ByteArray(length - (packetId.toString().length + 1))
                input.readFully(data)

                Bullet.logger.info("Received packet with ID $packetId and length $length from ${socket.remoteAddress}")
            }
        } catch(e: Exception) {
            Bullet.logger.warn("Session with ${socket.remoteAddress} ended: ${e.message}")
        } finally {
            socket.close()
        }
    }

    suspend fun readVarInt(): Int {
        var numRead = 0
        var result = 0
        while(true) {
            val read = input.readByte().toInt() and 0xFF
            result = result or ((read and 0x7F) shl (7 * numRead))
            numRead++
            if(numRead > 5) error("VarInt is too big")
            if((read and 0x80) == 0) break
        }

        return result
    }
}
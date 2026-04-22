package com.maddoxh.bullet

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.state.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

class ClientConnection(private val socket: Socket) {
    var state = ConnectionState.HANDSHAKE

    private val input = MinecraftInputStream(socket.getInputStream().buffered())
    private val output = MinecraftOutputStream(socket.getOutputStream())

    suspend fun handle() = withContext(Dispatchers.IO) {
        logger.info("[+] Client connected: ${socket.inetAddress}")

        try {
            while(!socket.isClosed) {
                handleNextPacket()
            }
        } catch(e: Exception) {
            logger.info("[-] Client disconnected: ${socket.inetAddress}")
        } finally {
            socket.close()
        }
    }

    private fun handleNextPacket() {
        val packetLength = input.readVarInt()
        val packetID = input.readVarInt()

        val idSize = varIntSize(packetID)
        val payloadLength = packetLength - idSize

        logger.info("[>] state=$state packetId=0x${packetID.toString(16)} payloadLen=$payloadLength")
    }

    private fun varIntSize(value: Int): Int = when {
        value and (0.inv() shl 7)  == 0 -> 1
        value and (0.inv() shl 14) == 0 -> 2
        value and (0.inv() shl 21) == 0 -> 3
        value and (0.inv() shl 28) == 0 -> 4
        else                            -> 5
    }
}
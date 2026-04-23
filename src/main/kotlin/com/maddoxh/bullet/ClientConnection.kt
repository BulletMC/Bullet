package com.maddoxh.bullet

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.entity.player.Player
import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.io.VarInt.varIntSize
import com.maddoxh.bullet.network.handler.HandshakeHandler
import com.maddoxh.bullet.network.handler.LoginHandler
import com.maddoxh.bullet.network.handler.PacketHandler
import com.maddoxh.bullet.network.handler.StatusHandler
import com.maddoxh.bullet.network.packet.PacketReader
import com.maddoxh.bullet.network.packet.PacketWriter
import com.maddoxh.bullet.network.packet.impl.out.OutboundPacket
import com.maddoxh.bullet.state.ConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger

class ClientConnection(private val socket: Socket, private val bullet: Bullet) {
    var state = ConnectionState.HANDSHAKE
    var player: Player? = null

    val isClosed: Boolean get() = socket.isClosed

    private val input = MinecraftInputStream(socket.getInputStream().buffered())
    private val output = MinecraftOutputStream(socket.getOutputStream())

    private val handlers: Map<ConnectionState, PacketHandler> = mapOf(
        ConnectionState.HANDSHAKE to HandshakeHandler(),
        ConnectionState.STATUS    to StatusHandler(bullet),
        ConnectionState.LOGIN     to LoginHandler()
    )

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
        val payloadLength = packetLength - varIntSize(packetID)

        logger.info("[>] state=$state packetId=0x${packetID.toString(16)} payloadLen=$payloadLength")

        val packet = PacketReader.read(state, packetID, payloadLength, input) ?: return
        handlers[state]?.handle(packet, this)
    }

    fun send(packet: OutboundPacket) {
        val (packetID, payload) = PacketWriter.serialize(packet)
        synchronized(output) {
            output.writePacket(packetID, payload)
            output.flush()
        }
    }

    companion object {
        private val entityIDCounter = AtomicInteger(1)
        fun nextEntityID(): Int = entityIDCounter.getAndIncrement()
    }
}

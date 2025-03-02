package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketRegistry
import com.aznos.packets.play.out.ServerKeepAlivePacket
import java.io.DataInputStream
import java.net.Socket
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

/**
 * Represents a session between a connected client and the server
 *
 * @property socket The clients socket connection
 */
class ClientSession(
    private val socket: Socket,
) : AutoCloseable {
    private val out = socket.getOutputStream()
    private val input = DataInputStream(socket.getInputStream())
    private val handler = PacketHandler(this)

    var state = GameState.HANDSHAKE
    var protocol = -1

    var username: String? = null
    var uuid: UUID? = null

    /**
     * This timer will keep track of when to send the keep alive packet to the client
     */

    private val keepAliveTimer = Timer(true).scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            sendPacket(ServerKeepAlivePacket(System.currentTimeMillis()))
        }
    }, 20.seconds.inWholeMilliseconds, 20.seconds.inWholeMilliseconds)

    /**
     * Reads and processes incoming packets from the client
     * It reads the packet length, ID, and data, then dispatches the packet to the handler
     */
    fun handle() {
        while (!isClosed()) {
            val len = input.readVarInt()
            val id = input.readVarInt()
            val dataLength = len - VarInt.getVarIntSize(id)
            val data = ByteArray(dataLength)
            input.readFully(data)

            val packetClass = PacketRegistry.getPacket(state, id)
            if (packetClass != null) {
                val packet: Packet = packetClass
                    .getConstructor(ByteArray::class.java)
                    .newInstance(data)
                handler.handle(packet)
            } else {
                println("No registered packet for state $state with id $id")
            }
        }
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        out.write(packet.retrieveData())
        out.flush()
    }

    /**
     * Checks if the clients socket is closed or not bound
     *
     * @return true If the socket is closed
     */
    private fun isClosed(): Boolean {
        return socket.let {
            it.isClosed || !it.isBound
        }
    }

    /**
     * Closes connection
     */
    override fun close() {
        socket.close()
    }
}
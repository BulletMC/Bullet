package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.Player
import com.aznos.events.EventManager
import com.aznos.events.PlayerQuitEvent
import com.aznos.packets.Packet
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketRegistry
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.packets.play.out.ServerChatMessagePacket
import com.aznos.packets.play.out.ServerKeepAlivePacket
import com.aznos.packets.play.out.ServerPlayDisconnectPacket
import com.aznos.entity.player.data.ChatPosition
import com.aznos.packets.data.PlayerInfo
import com.aznos.packets.play.out.ServerPlayerInfoPacket
import com.aznos.packets.play.out.ServerSpawnPlayerPacket
import com.aznos.packets.status.LegacyPingRequest
import net.kyori.adventure.text.TextComponent
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a session between a connected client and the server
 *
 * @param socket The clients socket connection
 * @property out The output stream to send packets to the client
 * @property input The input stream to read packets from the client
 * @property handler The packet handler to handle incoming packets
 */
class ClientSession(
    private val socket: Socket,
) : AutoCloseable {
    private val out = socket.getOutputStream()
    private val input = DataInputStream(BufferedInputStream(socket.getInputStream()))
    private val handler = PacketHandler(this)

    var state = GameState.HANDSHAKE
    var protocol = -1

    lateinit var player: Player

    /**
     * This timer will keep track of when to send the keep alive packet to the client
     */
    private var keepAliveTimer: Timer? = null
    var respondedToKeepAlive: Boolean = true

    /**
     * Reads and processes incoming packets from the client
     * It reads the packet length, ID, and data, then dispatches the packet to the handler
     */
    fun handle() {
        try {
            input.mark(10)
            val firstByte = input.readUnsignedByte()
            input.reset()

            if(firstByte == 0xFE) {
                val available = input.available()
                if(available == 0) { //Pre 1.4
                    LegacyPingRequest.handleBetaPing(out, this)
                    return
                } else {
                    input.readByte()
                    val secondByte = input.readByte().toInt()
                    if(secondByte == 0x01) { //1.6
                        LegacyPingRequest.handle16Ping(out, this)
                        return
                    } else { //Unknown
                        LegacyPingRequest.handleBetaPing(out, this)
                        return
                    }
                }
            }
        } catch(e: IOException) {
            disconnect("Invalid packet")
            return
        }

        try {
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
                    Bullet.logger.warn("Unhandled packet with raw packet ID: 0x$id (Hex: 0x${id.toString(16)})")
                }
            }
        } catch (e: EOFException) {
            disconnect("Client closed the connection")
        } catch (e: SocketException) {
            disconnect("Connection lost")
        }
    }

    fun scheduleKeepAlive() {
        keepAliveTimer = Timer(true).apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if(isClosed()) {
                        cancel()
                        return
                    }

                    if(!respondedToKeepAlive) {
                        disconnect("Timed out")
                        cancel()
                        return
                    }
                    sendPacket(ServerKeepAlivePacket(System.currentTimeMillis()))
                    respondedToKeepAlive = true
                }
            }, 10.seconds.inWholeMilliseconds, 10.seconds.inWholeMilliseconds)
        }
    }

    /**
     * Disconnects the client that is in the play state with the server play disconnect packet
     *
     * @param message The message to be sent to the client
     */
    fun disconnect(message: String) {
        if(state == GameState.PLAY) {
            sendPacket(ServerPlayDisconnectPacket(message))
        } else if(state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        keepAliveTimer?.cancel()
        keepAliveTimer = null

        for(session in Bullet.players) {
            session.clientSession.sendPacket(
                ServerPlayerInfoPacket(
                    4,
                    listOf(
                        PlayerInfo(
                            player.uuid,
                            player.username
                        )
                    )
                )
            )
        }

        Bullet.players.remove(player)

        EventManager.fire(PlayerQuitEvent(player.username))
        close()
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        if(isClosed()) {
            Bullet.logger.warn("Tried to send a packet to a closed connection")
            return
        }

        out.write(packet.retrieveData())
        out.flush()
    }

    fun sendPlayerSpawnPacket() {
        for(otherPlayer in Bullet.players) {
            if(otherPlayer.clientSession != this) {
                otherPlayer.clientSession.sendPacket(
                    ServerPlayerInfoPacket(
                        0,
                        listOf(
                            PlayerInfo(
                                player.uuid,
                                player.username,
                                gameMode = player.gameMode,
                                ping = 50,
                                hasDisplayName = false
                            )
                        )
                    )
                )

                otherPlayer.clientSession.sendPacket(
                    ServerSpawnPlayerPacket(
                        player.entityID,
                        player.uuid,
                        player.location.x,
                        player.location.y,
                        player.location.z,
                        player.location.yaw,
                        player.location.pitch
                    )
                )

                sendPacket(
                    ServerPlayerInfoPacket(
                        0,
                        listOf(
                            PlayerInfo(
                                otherPlayer.uuid,
                                otherPlayer.username,
                                gameMode = otherPlayer.gameMode,
                                ping = 50,
                                hasDisplayName = false
                            )
                        )
                    )
                )

                sendPacket(
                    ServerSpawnPlayerPacket(
                        otherPlayer.entityID,
                        otherPlayer.uuid,
                        otherPlayer.location.x,
                        otherPlayer.location.y,
                        otherPlayer.location.z,
                        otherPlayer.location.yaw,
                        otherPlayer.location.pitch
                    )
                )
            }
        }
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
        keepAliveTimer = null
        socket.close()
    }
}
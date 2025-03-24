package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.EventManager
import com.aznos.events.PlayerQuitEvent
import com.aznos.packets.Packet
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketRegistry
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.packets.play.out.*
import com.aznos.packets.status.LegacyPingRequest
import com.aznos.world.blocks.Block
import com.aznos.world.data.Difficulty
import net.kyori.adventure.text.Component
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.math.max
import kotlin.math.min
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
    val input = DataInputStream(BufferedInputStream(socket.getInputStream()))
    private val handler = PacketHandler(this)

    var state = GameState.HANDSHAKE
    var protocol = -1

    lateinit var player: Player

    /**
     * This timer will keep track of when to send the keep alive packet to the client
     */
    private var keepAliveTimer: Timer? = null
    private var halfSecondTimer: Timer? = null
    private var lastKeepAliveTimestamp: Long = 0
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
            disconnect(Component.text("Invalid packet"))
            return
        }

        try {
            while(!isClosed()) {
                val len = input.readVarInt()
                val id = input.readVarInt()
                val dataLength = len - VarInt.getVarIntSize(id)

                val data = ByteArray(dataLength)
                input.readFully(data)

                val packetClass = PacketRegistry.getPacket(state, id)
                if(packetClass != null) {
                    val packet: Packet = packetClass
                        .getConstructor(ByteArray::class.java)
                        .newInstance(data)
                    handler.handle(packet)
                } else {
                    Bullet.logger.warn("Unhandled packet with raw packet ID: 0x$id (Hex: 0x${id.toString(16)})")
                }
            }
        } catch(e: EOFException) {
            disconnect(Component.text("Connection lost"))
        } catch(e: SocketException) {
            disconnect(Component.text("Connection lost"))
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
                        disconnect(Component.text("Timed out"))
                        cancel()
                        return
                    }

                    lastKeepAliveTimestamp = System.currentTimeMillis()
                    sendPacket(ServerKeepAlivePacket(lastKeepAliveTimestamp))
                    respondedToKeepAlive = true
                }
            }, 10.seconds.inWholeMilliseconds, 10.seconds.inWholeMilliseconds)
        }
    }

    /**
     * Called every half second to update player information, like health, food, etc
     */
    fun scheduleHalfSecondUpdate() {
        halfSecondTimer = Timer(true).apply {
            var timeSinceHealthUpdate = 0
            var timeSinceHealthDecrease = 0

            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if(isClosed()) {
                        cancel()
                        return
                    }

                    if(player.status.exhaustion >= 4) {
                        player.status.exhaustion -= 4
                        if(player.status.saturation >= 1) player.status.saturation -= 1
                        else player.status.foodLevel -= 1
                    }

                    if(player.gameMode == GameMode.SURVIVAL) {
                        if(
                            player.status.foodLevel == 20 &&
                            player.status.saturation > 0 &&
                            player.status.health != 20
                        ) {
                            player.status.health = min(player.status.health + 1, 20)
                            player.status.exhaustion += 6.0f
                        } else if(player.status.foodLevel > 18 && player.status.health != 20) {
                            if(timeSinceHealthUpdate == 4000) {
                                player.status.health = min(player.status.health + 1, 20)
                                timeSinceHealthUpdate = 0
                                player.status.exhaustion += 6.0f
                            }
                        }

                        if(player.status.foodLevel == 0 && player.status.health > 0) {
                            if(timeSinceHealthDecrease == 4000) {
                                when(Bullet.world.difficulty) {
                                    Difficulty.PEACEFUL -> {}
                                    Difficulty.EASY -> player.status.health = max(player.status.health - 1, 10)
                                    Difficulty.NORMAL -> player.status.health = max(player.status.health - 1, 1)
                                    Difficulty.HARD -> player.status.health -= 1
                                }

                                timeSinceHealthDecrease = 0
                            }
                        }

                        if(player.world?.difficulty == Difficulty.PEACEFUL && player.status.foodLevel != 20) {
                            player.status.foodLevel = min(player.status.foodLevel + 1, 20)
                        }
                    }

                    player.sendPacket(ServerUpdateHealthPacket(
                        player.status.health.toFloat(),
                        player.status.foodLevel,
                        player.status.saturation)
                    )

                    timeSinceHealthUpdate += 500
                    timeSinceHealthDecrease += 500
                }
            }, 0, 500)
        }
    }

    /**
     * Disconnects the client that is in the play state with the server play disconnect packet
     *
     * @param message The message to be sent to the client
     */
    fun disconnect(message: Component) {
        if(isClosed()) return

        if(state == GameState.PLAY) {
            sendPacket(ServerPlayDisconnectPacket(message))
        } else if(state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        keepAliveTimer?.cancel()
        halfSecondTimer?.cancel()
        keepAliveTimer = null
        halfSecondTimer = null

        Bullet.players.remove(player)

        for(player in Bullet.players) {
            player.sendPacket(
                ServerPlayerInfoPacket(
                    4,
                    player
                )
            )
        }

        EventManager.fire(PlayerQuitEvent(player))
        Bullet.players.remove(player)

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

        try {
            out.write(packet.retrieveData())
            out.flush()
        } catch(e: IOException) {
            Bullet.logger.warn("Failed to send packet to client: ${e.message}")
            disconnect(Component.text("Connection lost"))
        }
    }

    fun sendPlayerSpawnPacket() {
        for(otherPlayer in Bullet.players) {
            if(otherPlayer.clientSession != this) {
                otherPlayer.sendPacket(ServerPlayerInfoPacket(0, player))

                otherPlayer.sendPacket(
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

                sendPacket(ServerPlayerInfoPacket(0, otherPlayer))

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

        sendPacket(ServerPlayerInfoPacket(0, player))
    }

    fun updatePlayerChunks(chunkX: Int, chunkZ: Int) {
        val viewDistance = player.viewDistance
        val newChunks = mutableSetOf<Pair<Int, Int>>()

        for(dx in -viewDistance..viewDistance) {
            for(dz in -viewDistance..viewDistance) {
                newChunks.add(Pair(chunkX + dx, chunkZ + dz))
            }
        }

        val chunksToLoad = newChunks - player.loadedChunks
        val chunksToUnload = player.loadedChunks - newChunks

        for(chunk in chunksToLoad) {
            sendPacket(ServerChunkPacket(chunk.first, chunk.second))
        }

        for(chunk in chunksToUnload) {
            sendPacket(ServerUnloadChunkPacket(chunk.first, chunk.second))
        }

        player.loadedChunks.clear()
        player.loadedChunks.addAll(newChunks)
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
        keepAliveTimer?.cancel()
        halfSecondTimer?.cancel()

        keepAliveTimer = null
        halfSecondTimer = null

        Bullet.players.remove(player)

        socket.close()
    }
}
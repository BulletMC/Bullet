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
import com.aznos.world.data.Difficulty
import kotlinx.coroutines.*
import net.kyori.adventure.text.Component
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.IOException
import java.net.Socket
import java.net.SocketException
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
@Suppress("TooManyFunctions")
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
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
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
        coroutineScope.launch {
            while(isActive) {
                delay(10.seconds)

                if(isClosed()) return@launch
                if(!respondedToKeepAlive) {
                    disconnect(Component.text("Timed out"))
                    return@launch
                }

                lastKeepAliveTimestamp = System.currentTimeMillis()
                sendPacket(ServerKeepAlivePacket(lastKeepAliveTimestamp))
                respondedToKeepAlive = false
            }
        }
    }

    /**
     * Called every half second to update player information, like health, food, etc
     */
    fun scheduleHalfSecondUpdate() {
        coroutineScope.launch {
            var timeSinceHealthUpdate = 0
            var timeSinceHealthDecrease = 0

            while(isActive) {
                if(isClosed()) break

                updatePlayerStatus()
                val (newUpdate, newDecrease) = updatePlayerHealth(timeSinceHealthUpdate, timeSinceHealthDecrease)

                timeSinceHealthUpdate = newUpdate
                timeSinceHealthDecrease = newDecrease

                delay(500)
            }
        }
    }

    fun scheduleSaving() {
        coroutineScope.launch {
            while(isActive) {
                delay(5.seconds)

                Bullet.world.writePlayerData(
                    player.username,
                    player.uuid,
                    player.location,
                    player.status.health,
                    player.status.foodLevel,
                    player.status.saturation,
                    player.status.exhaustion
                )
            }
        }
    }

    private fun updatePlayerStatus() {
        with(player.status) {
            if(exhaustion >= 4) {
                exhaustion -= 4
                if(saturation >= 1) saturation -= 1
                else foodLevel -= 1
            }
        }
    }

    private fun updatePlayerHealth(
        timeSinceHealthUpdate: Int,
        timeSinceHealthDecrease: Int
    ) : Pair<Int, Int> {
        var newUpdate = timeSinceHealthUpdate
        var newDecrease = timeSinceHealthDecrease

        with(player.status) {
            if(player.gameMode == GameMode.SURVIVAL) {
                newUpdate = handleHealthRegeneration(newUpdate)
                newDecrease = handleHealthDecrease(newDecrease)
                handlePeacefulMode()
            }

            player.sendPacket(
                ServerUpdateHealthPacket(
                    health.toFloat(), foodLevel, saturation
                )
            )
        }

        return newUpdate to newDecrease
    }

    private fun handleHealthRegeneration(time: Int): Int {
        with(player.status) {
            if(foodLevel == 20 && saturation > 0 && health != 20) {
                health = min(health + 1, 20)
                exhaustion += 6.0f
                return time
            } else if (foodLevel > 18 && health != 20) {
                if(time == 4000) {
                    health = min(health + 1, 20)
                    exhaustion += 6.0f
                    return 0
                }
            }
        }

        return time
    }

    private fun handleHealthDecrease(time: Int): Int {
        with(player.status) {
            if(foodLevel == 0 && health > 0) {
                if(time == 4000) {
                    health -= when(Bullet.world.difficulty) {
                        Difficulty.PEACEFUL -> 0
                        Difficulty.EASY -> max(health - 1, 10)
                        Difficulty.NORMAL -> max(health - 1, 1)
                        Difficulty.HARD -> 1
                    }

                    return 0
                }
            }
        }

        return time
    }

    private fun handlePeacefulMode() {
        with(player.status) {
            if(player.world?.difficulty == Difficulty.PEACEFUL && foodLevel != 20) {
                foodLevel = min(foodLevel + 1, 20)
            }
        }
    }

    /**
     * Disconnects the client that is in the play state with the server play disconnect packet
     *
     * @param message The message to be sent to the client
     */
    fun disconnect(message: Component) {
        if(state == GameState.PLAY) {
            sendPacket(ServerPlayDisconnectPacket(message))
        } else if(state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        coroutineScope.cancel()
        Bullet.players.remove(player)

        for(plr in Bullet.players) {
            plr.sendPacket(
                ServerPlayerInfoPacket(
                    4,
                    plr
                )
            )

            plr.sendPacket(
                ServerDestroyEntitiesPacket(
                    intArrayOf(player.entityID)
                )
            )
        }

        EventManager.fire(PlayerQuitEvent(player))
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
        coroutineScope.cancel()
        Bullet.players.remove(player)

        socket.close()
    }
}
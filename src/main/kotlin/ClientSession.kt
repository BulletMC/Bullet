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
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a session between a connected client and the server
 *
 * @param socket The clients socket connection
 *
 * @property out The output stream to send packets to the client
 * @property input The input stream to read packets from the client
 * @property handler The packet handler to handle incoming packets
 * @property state The current state of the game connection
 * @property protocol The protocol version of the player
 * @property player The player class associated with this session
 *
 * @property coroutineScope The coroutine scope to manage the coroutines
 * @property lastKeepAliveTimestamp The last time a keep alive packet was sent
 * @property respondedToKeepAlive If the client has responded to the last keep alive packet
 */
@Suppress("TooManyFunctions")
class ClientSession(
    private val socket: Socket,
) : AutoCloseable {
    private var out = socket.getOutputStream()
    var input = DataInputStream(BufferedInputStream(socket.getInputStream()))
    private val handler = PacketHandler(this)

    var state = GameState.HANDSHAKE
    var protocol = -1

    lateinit var player: Player
    lateinit var verifyToken: ByteArray

    /**
     * This timer will keep track of when to send the keep alive packet to the client
     */
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var lastKeepAliveTimestamp: Long = 0
    var respondedToKeepAlive: Boolean = true

    private val sendLock = Any()

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

    fun enableEncryption(dec: Cipher, enc: Cipher) {
        input = DataInputStream(CipherInputStream(socket.inputStream, dec))
        out = CipherOutputStream(socket.outputStream, enc)
    }

    /**
     * Schedules a keep alive session with the player, this is sent every 10 seconds to check
     * if the player is still connected
     */
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

    /**
     * Schedules saving the player data every 5 seconds
     * This is so that it can save things like player location, health, etc
     */
    fun scheduleSaving() {
        if(Bullet.shouldPersist) {
            coroutineScope.launch {
                while(isActive) {
                    delay(5.seconds)

                    Bullet.storage.storage.writePlayerData(player)
                }
            }
        }
    }

    /**
     * Helper function to update the player exhaustion, saturation, and food level
     */
    private fun updatePlayerStatus() {
        with(player.status) {
            if(exhaustion >= 4) {
                exhaustion -= 4
                if(saturation >= 1) saturation -= 1
                else foodLevel -= 1
            }
        }
    }

    /**
     * Helper function to handle what happens when the food level is at a certain point
     *
     * @param timeSinceHealthUpdate The time since the last health update
     * @param timeSinceHealthDecrease The time since the last health decrease
     */
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

    /**
     * Helper function to regenerate the players health
     *
     * @param time The time since the last health update
     */
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

    /**
     * Helper function to decrease the players health if the food level is low
     *
     * @param time The time since the last health decrease
     */
    private fun handleHealthDecrease(time: Int): Int {
        with(player.status) {
            if(foodLevel <= 0 && health > 0) {
                if(time >= 4000) {
                    health -= when(player.world!!.difficulty) {
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

    /**
     * Helper function to handle peaceful mode when the health bar isn't at full
     * If it's not, it will increase the food level until it's full
     */
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
        if(state == GameState.DISCONNECTED) return
        if(state == GameState.PLAY && this::player.isInitialized) {
            Bullet.storage.storage.writePlayerData(player)

            sendPacket(ServerPlayDisconnectPacket(message))

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
        } else if(state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        state = GameState.DISCONNECTED
        coroutineScope.cancel()

        if(this::player.isInitialized) EventManager.fire(PlayerQuitEvent(player))
        close()
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        synchronized(sendLock) {
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
    }

    /**
     * Sends the player spawn position information to the client when the player logs into the server
     * This also sends the player information to all other players on the server
     */
    fun sendPlayerSpawnPacket() {
        for(otherPlayer in Bullet.players) {
            if(otherPlayer.clientSession != this) {
                otherPlayer.sendPacket(ServerPlayerInfoPacket(0, player))

                otherPlayer.sendPacket(
                    ServerSpawnPlayerPacket(
                        player.entityID,
                        player.uuid,
                        player.location
                    )
                )

                sendPacket(ServerPlayerInfoPacket(0, otherPlayer))

                sendPacket(
                    ServerSpawnPlayerPacket(
                        otherPlayer.entityID,
                        otherPlayer.uuid,
                        otherPlayer.location
                    )
                )
            }
        }

        sendPacket(ServerPlayerInfoPacket(0, player))
    }

    /**
     * Updates the chunks visible to the player based on their position
     * It will also unload chunks as needed
     *
     * @param chunkX The chunk X position of the player (position / 16)
     * @param chunkZ The chunk Z position of the player (position / 16)
     */
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
        // player not yet init on HANDSHAKE or STATUS
        if (state != GameState.HANDSHAKE && state != GameState.STATUS) {
            Bullet.players.remove(player)
        }

        socket.close()
    }
}
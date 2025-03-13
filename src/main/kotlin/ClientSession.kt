package com.aznos

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.Player
import com.aznos.events.EventManager
import com.aznos.events.PlayerQuitEvent
import com.aznos.packets.Packet
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketRegistry
import com.aznos.packets.configuration.out.ServerConfigRegistryData
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.packets.data.PlayerInfo
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.newPacket.ResourceLocation
import com.aznos.packets.newPacket.ServerPacket
import com.aznos.packets.play.out.*
import com.aznos.packets.play.out.chat.ServerSystemChatMessagePacket
import com.aznos.packets.play.out.entity.ServerSpawnEntityPacket
import com.aznos.packets.status.LegacyPingRequest
import com.aznos.registry.Registries
import com.aznos.registry.Registry
import dev.dewy.nbt.tags.collection.CompoundTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import java.io.*
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

            if (firstByte == 0xFE) {
                val available = input.available()
                if (available == 0) { //Pre 1.4
                    LegacyPingRequest.handleBetaPing(out, this)
                    return
                } else {
                    input.readByte()
                    val secondByte = input.readByte().toInt()
                    if (secondByte == 0x01) { //1.6
                        LegacyPingRequest.handle16Ping(out, this)
                        return
                    } else { //Unknown
                        LegacyPingRequest.handleBetaPing(out, this)
                        return
                    }
                }
            }
        } catch (e: IOException) {
            disconnect(Component.text("Invalid packet"))
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
                    Bullet.logger.warning("Unhandled packet with raw packet ID: 0x$id (Hex: 0x${id.toString(16)})")
                }
            }
        } catch (e: EOFException) {
            disconnect(Component.text("Client closed the connection"))
        } catch (e: SocketException) {
            disconnect(Component.text("Connection lost"))
        }
    }

    fun scheduleKeepAlive() {
        keepAliveTimer = Timer(true).apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    if (isClosed()) {
                        cancel()
                        return
                    }

                    if (!respondedToKeepAlive) {
                        disconnect(Component.text("Timed out"))
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
     * Sends a chat message to the client
     *
     * @param message The message to be sent to the client
     */
    fun sendMessage(message: TextComponent) {
        sendPacket(ServerSystemChatMessagePacket(message, false))
    }

    fun disconnect(message: String) {
        disconnect(Component.text(message))
    }

    /**
     * Disconnects the client that is in the play state with the server play disconnect packet
     *
     * @param message The message to be sent to the client
     */
    fun disconnect(message: Component) {
        if (state == GameState.PLAY) {
            sendPacket(ServerPlayDisconnectPacket(message))
        } else if (state == GameState.LOGIN) {
            sendPacket(ServerLoginDisconnectPacket(message))
        }

        keepAliveTimer?.cancel()
        keepAliveTimer = null

        for (session in Bullet.players) {
            session.clientSession.sendPacket(
                ServerPlayerInfoUpdatePacket(
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

    fun sendRegistries() {
        sendPacket(ServerConfigRegistryData(Registries.dimension_type))
        sendPacket(ServerConfigRegistryData(Registries.biomes))
        sendPacket(ServerConfigRegistryData(Registries.wolf_variant))
        sendPacket(ServerConfigRegistryData(Registries.damage_type))
        sendPacket(ServerConfigRegistryData(Registries.painting_variant))
//
//        sendPacket(
//            ServerConfigRegistryData(
//                ResourceLocation.vanilla("painting_variant"), listOf(
//                    ServerConfigRegistryData.RawEntry(ResourceLocation.vanilla("alban"), CompoundTag().apply {
//                        putString("asset_id", "minecraft:alban")
//                        putInt("height", 1)
//                        putInt("width", 1)
//                        putString("title", "gg")
//                        putString("author", "gg")
//                    })
//                )
//            )
//        )
    }

    fun isClientValid(packet: ClientLoginStartPacket): Boolean {
        if (protocol > Bullet.PROTOCOL) {
            disconnect(Component.text("Please downgrade your minecraft version to " + Bullet.VERSION))
            return false
        } else if (protocol < Bullet.PROTOCOL) {
            disconnect(Component.text("Your client is outdated, please upgrade to minecraft version " + Bullet.VERSION))
            return false
        }

        val username = packet.username
        if (!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) { // Alphanumeric and 3-16 characters
            disconnect(Component.text("Invalid username"))
            return false
        }

        return true
    }

    /**
     * Sends a packet to the client
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(serverPacket: ServerPacket) {
        if (isClosed()) {
            Bullet.logger.warning("Tried to send a packet to a closed connection")
            return
        }

        val packetData = serverPacket.retrieveData()
        val packetId = serverPacket.resourceLocation.id

        val baos = ByteArrayOutputStream()
        val packetOut = DataOutputStream(baos)

        packetOut.writeVarInt(packetData.size + VarInt.getVarIntSize(packetId))
        packetOut.writeVarInt(packetId)
        packetOut.write(packetData)

        out.write(baos.toByteArray())
        out.flush()
    }

    fun sendPlayerSpawnPacket() {
        for (otherPlayer in Bullet.players) {
            if (otherPlayer.clientSession != this) {
                otherPlayer.clientSession.sendPacket(
                    ServerPlayerInfoUpdatePacket(
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
                    ServerSpawnEntityPacket(
                        player.entityID,
                        player.uuid,
                        147,
                        player.location.x,
                        player.location.y,
                        player.location.z,
                        player.location.yaw,
                        player.location.pitch,
                        player.location.yaw,
                        0,
                        0,
                        0, 0
                    )
                )

                sendPacket(
                    ServerPlayerInfoUpdatePacket(
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
                    ServerSpawnEntityPacket(
                        otherPlayer.entityID,
                        otherPlayer.uuid,
                        147,
                        otherPlayer.location.x,
                        otherPlayer.location.y,
                        otherPlayer.location.z,
                        otherPlayer.location.yaw,
                        otherPlayer.location.pitch,
                        otherPlayer.location.yaw,
                        0,
                        0,
                        0, 0
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
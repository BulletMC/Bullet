package com.aznos.packets

import com.aznos.Bullet
import com.aznos.Bullet.breakingBlocks
import com.aznos.Bullet.sprinting
import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandManager
import com.aznos.commands.CommandManager.buildCommandGraphFromDispatcher
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.MetadataType
import com.aznos.datatypes.Slot
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.*
import com.aznos.packets.data.ServerStatusResponse
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.out.ServerLoginSuccessPacket
import com.aznos.packets.play.`in`.*
import com.aznos.packets.play.`in`.movement.*
import com.aznos.packets.play.out.*
import com.aznos.packets.play.out.movement.*
import com.aznos.packets.status.`in`.ClientStatusPingPacket
import com.aznos.packets.status.`in`.ClientStatusRequestPacket
import com.aznos.packets.status.out.ServerStatusPongPacket
import com.aznos.world.blocks.Block
import com.aznos.util.DurationFormat
import com.aznos.world.World
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.BlockStatus
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.Particles
import com.aznos.world.items.Item
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import packets.handshake.HandshakePacket
import packets.status.out.ServerStatusResponsePacket
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.text.html.HTML.Tag.I
import kotlin.experimental.and
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Handles all incoming packets by dispatching them to the appropriate handler methods
 *
 * @property client The clients session
 */
@Suppress("UnusedParameter", "TooManyFunctions", "LargeClass")
class PacketHandler(
    private val client: ClientSession
) {

    val world: World
        get() = client.player.world!!

    @PacketReceiver
    fun onUpdateSign(packet: ClientUpdateSignPacket) {
        val data = CompoundTag("")
        data.putString("id", "minecraft:sign")
        data.putInt("x", packet.blockPos.x.toInt())
        data.putInt("y", packet.blockPos.y.toInt())
        data.putInt("z", packet.blockPos.z.toInt())

        data.putString("Text1", "{\"text\":\"${packet.line1}\"}")
        data.putString("Text2", "{\"text\":\"${packet.line2}\"}")
        data.putString("Text3", "{\"text\":\"${packet.line3}\"}")
        data.putString("Text4", "{\"text\":\"${packet.line4}\"}")

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerBlockEntityDataPacket(
                    packet.blockPos,
                    9,
                    data
                ))
            }
        }

        val world = world
        val prev = world.modifiedBlocks[packet.blockPos]
        if(prev != null) {
            val lines = listOf(packet.line1, packet.line2, packet.line3, packet.line4)
            world.modifiedBlocks[packet.blockPos] = prev.copy(textLines = lines)
        }
    }

    @PacketReceiver
    fun onPlayerAbilities(packet: ClientPlayerAbilitiesPacket) {
        if(client.player.canFly) {
            val flying = (packet.flags and 0x02).toInt() == 0x02
            client.player.isFlying = flying
        } else {
            client.player.isFlying = false
            client.player.sendPacket(ServerPlayerAbilitiesPacket(
                0,
                0f,
            ))
        }
    }

    @PacketReceiver
    fun onTabComplete(packet: ClientTabCompletePacket) {
        val dispatcher = CommandManager.dispatcher
        val rawInput = packet.text
        val input = if (rawInput.startsWith("/")) rawInput.substring(1) else rawInput

        val parseResults = dispatcher.parse(input, client.player)
        dispatcher.getCompletionSuggestions(parseResults, input.length).thenAccept { suggestions ->
            val lastSpace = input.lastIndexOf(' ')
            val start = lastSpace + 1
            val length = input.length - start

            val startStr = input.substring(start)

            val matches = suggestions.list
                .filter { it.text.startsWith(startStr, ignoreCase = true) }
                .map { it.text }

            val formattedMatches = matches.map { match ->
                if (lastSpace == -1) "/$match" else match
            }

            client.player.sendPacket(ServerTabCompletePacket(
                packet.transactionID,
                start = start + 1,
                length = length,
                matches = formattedMatches
            ))
        }
    }

    @PacketReceiver
    fun onClientStatus(packet: ClientStatusPacket) {
        when(packet.actionID) {
            0 -> { // Perform respawn
                client.player.sendPacket(ServerRespawnPacket(
                    Bullet.dimensionCodec!!,
                    "minecraft:overworld",
                    GameMode.SURVIVAL,
                    false,
                    false,
                    true
                ))

                client.player.status.health = 20
                client.player.status.foodLevel = 20
                client.player.status.saturation = 5.0f
                client.player.status.exhaustion = 0f

                client.player.sendPacket(
                    ServerPlayerPositionAndLookPacket(
                        LocationType.Location(8.5, 2.0, 8.5)
                    )
                )
            }

            1 -> { // Request statistics

            }
        }
    }

    @PacketReceiver
    fun onEntityInteract(packet: ClientInteractEntityPacket) {
        val attacker = client.player

        val event = PlayerInteractEntityEvent(attacker, packet.entityID, packet.type)
        EventManager.fire(event)
        if(event.isCancelled) return

        if(packet.type == 1) {
            for(player in Bullet.players) {
                if(player.entityID == packet.entityID && player.gameMode == GameMode.SURVIVAL) {
                    player.status.health -= 1

                    player.sendPacket(ServerUpdateHealthPacket(
                        player.status.health.toFloat(),
                        player.status.foodLevel,
                        player.status.saturation
                    ))

                    player.sendPacket(ServerAnimationPacket(
                        player.entityID,
                        1
                    ))

                    player.status.exhaustion += 0.1f

                    val dx = player.location.x - attacker.location.x
                    val dy = player.location.y - attacker.location.y
                    val dz = player.location.z - attacker.location.z
                    val distance = sqrt(dx * dx + dy * dy + dz * dz)
                    if(distance != 0.0) {
                        val kbStrength = 0.5

                        val kbX = (dx / distance) * kbStrength
                        val kbY = if(player.onGround) 0.3 else 0.125
                        val kbZ = (dz / distance) * kbStrength

                        player.sendPacket(ServerEntityVelocityPacket(
                            player.entityID,
                            (kbX * 8000).toInt().toShort(),
                            (kbY * 8000).toInt().toShort(),
                            (kbZ * 8000).toInt().toShort()
                        ))
                    }
                }
            }
        }
    }

    @PacketReceiver
    fun onHeldItemChange(packet: ClientHeldItemChangePacket) {
        val event = PlayerHeldItemChangeEvent(client.player, packet.slot.toInt())
        EventManager.fire(event)
        if(event.isCancelled) return

        client.player.selectedSlot = packet.slot.toInt()
        sendHeldItemUpdate()
    }

    @PacketReceiver
    fun onCreativeInventoryAction(packet: ClientCreativeInventoryActionPacket) {
        if(packet.slot.present) {
            packet.slot.itemID?.let { itemID ->
                client.player.inventory.items[packet.slotIndex.toInt()] = itemID
            }
        } else {
            client.player.inventory.items.remove(packet.slotIndex.toInt())
        }

        if(packet.slotIndex.toInt() == client.player.selectedSlot + 36) {
            sendHeldItemUpdate()
        }
    }

    @PacketReceiver
    fun onPluginMessage(packet: ClientPluginMessagePacket) {
        when(packet.channel) {
            "minecraft:brand" -> {
                val input = DataInputStream(ByteArrayInputStream(packet.pluginData))
                val length = input.readVarInt()

                val brandBytes = ByteArray(length)
                input.readFully(brandBytes)

                val brand = String(brandBytes, Charsets.UTF_8)
                client.player.brand = brand

                val event = PlayerBrandEvent(client.player, brand)
                EventManager.fire(event)
                if(event.isCancelled) {
                    client.player.disconnect(Component.text("Your client brand is not supported"))
                    return
                }
            }
        }
    }

    @PacketReceiver
    fun onPlayerSettingsChange(packet: ClientSettingsPacket) {
        val event = PlayerSettingsChangeEvent(
            client.player,
            packet.locale,
            packet.viewDistance.toInt(),
            packet.chatMode,
            packet.chatColors,
            packet.displayedSkinParts.toInt(),
            packet.mainHand
        )
        EventManager.fire(event)
        if(event.isCancelled) return

        client.player.viewDistance = packet.viewDistance.toInt()
        client.player.locale = packet.locale

        client.sendPacket(ServerUpdateViewPositionPacket(client.player.chunkX, client.player.chunkZ))
        client.updatePlayerChunks(client.player.chunkX, client.player.chunkZ)
    }

    /**
     * Called when a client performs an action, such as jumping, sneaking, or sprinting
     */
    @PacketReceiver
    fun onPlayerAction(packet: ClientEntityActionPacket) {
        when(packet.actionID) {
            0 -> { //Start sneaking
                val event = PlayerSneakEvent(client.player, true)
                EventManager.fire(event)
                if(event.isCancelled) return

                client.player.isSneaking = true
                updateEntityMetadata(client.player, 6, 5)
            }

            1 -> { //Stop sneaking
                val event = PlayerSneakEvent(client.player, false)
                EventManager.fire(event)
                if(event.isCancelled) return

                client.player.isSneaking = false
                updateEntityMetadata(client.player, 6, 0)
            }

            3 -> { //Start sprinting
                val event = PlayerSprintEvent(client.player, true)
                EventManager.fire(event)
                if(event.isCancelled) return

                sprinting.add(client.player.entityID)
                client.player.lastSprintLocation = client.player.location
            }

            4 -> { //Stop sprinting
                val event = PlayerSprintEvent(client.player, false)
                EventManager.fire(event)
                if(event.isCancelled) return

                sprinting.remove(client.player.entityID)
                client.player.lastSprintLocation = null
            }
        }
    }

    /**
     * Called when a client starts digging a block
     */
    @PacketReceiver
    fun onPlayerDig(packet: ClientDiggingPacket) {
        val event = BlockBreakEvent(
            client.player,
            packet.status,
            BlockPositionType.BlockPosition(packet.blockPos.x, packet.blockPos.y, packet.blockPos.z),
            packet.face
        )
        EventManager.fire(event)
        if(event.isCancelled) return

        if(client.player.gameMode == GameMode.CREATIVE && event.status == BlockStatus.STARTED_DIGGING.id) {
            for(otherPlayer in Bullet.players) {
                if(otherPlayer != client.player) {
                    otherPlayer.sendPacket(ServerBlockChangePacket(
                        event.blockPos,
                        0
                    ))

                    val block = world.modifiedBlocks[event.blockPos]?.blockID ?: Block.GRASS_BLOCK.id
                    sendBlockBreakParticles(otherPlayer, block, event.blockPos)
                }
            }

            removeBlock(event.blockPos)
        } else if(client.player.gameMode == GameMode.SURVIVAL) {
            when(event.status) {
                BlockStatus.STARTED_DIGGING.id -> {
                    val breakTime = getStoneBreakTime()
                    startBlockBreak(event.blockPos, breakTime.toInt())
                }

                BlockStatus.CANCELLED_DIGGING.id -> {
                    stopBlockBreak(event.blockPos)
                }

                BlockStatus.FINISHED_DIGGING.id -> {
                    client.player.status.exhaustion += 0.005f
                    val block = world.modifiedBlocks[event.blockPos]?.blockID ?: Block.GRASS_BLOCK.id

                    stopBlockBreak(event.blockPos)
                    sendBlockBreakParticles(client.player, block, event.blockPos)
                    removeBlock(event.blockPos)
                }
            }
        }
    }

    @PacketReceiver
    fun onArmSwing(packet: ClientAnimationPacket) {
        val event = PlayerArmSwingEvent(client.player)
        EventManager.fire(event)
        if(event.isCancelled) return

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerAnimationPacket(client.player.entityID, 0))
            }
        }
    }

    /**
     * Called when a client places a block
     */
    @PacketReceiver
    fun onBlockPlacement(packet: ClientBlockPlacementPacket) {
        val event = BlockPlaceEvent(
            client.player,
            packet.hand,
            packet.blockPos.copy(),
            packet.face,
            packet.cursorPositionX,
            packet.cursorPositionY,
            packet.cursorPositionZ,
            packet.insideBlock
        )
        EventManager.fire(event)
        if(event.isCancelled) return

        when(event.face) {
            0 -> event.blockPos.y -= 1
            1 -> event.blockPos.y += 1
            2 -> event.blockPos.z -= 1
            3 -> event.blockPos.z += 1
            4 -> event.blockPos.x -= 1
            5 -> event.blockPos.x += 1
        }

        val heldItem = client.player.getHeldItem()

        val block = Block.getBlockFromID(heldItem) ?: Item.getItemFromID(heldItem) ?: Block.AIR
        handlePlacement(block, event, heldItem)
    }

    /**
     * Every 20 ticks the client will send an empty movement packet telling the server if the
     * client is on the ground or not
     */
    @PacketReceiver
    fun onPlayerMovement(packet: ClientPlayerMovement) {
        val player = client.player
        player.onGround = packet.onGround

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(ServerEntityMovementPacket(player.entityID))
        }
    }

    /**
     * Handles when a player rotates to a new yaw and pitch
     */
    @PacketReceiver
    fun onPlayerRotation(packet: ClientPlayerRotation) {
        val newLocation = client.player.location.set(packet.yaw, packet.pitch)

        val event = PlayerMoveEvent(
            client.player,
            newLocation,
            client.player.location.copy()
        )
        EventManager.fire(event)
        if(event.isCancelled) return

        val player = client.player
        player.location = newLocation
        player.onGround = packet.onGround

        val rotPacket = ServerEntityRotationPacket(
            player.entityID,
            player.location.yaw,
            player.location.pitch,
            player.onGround
        )

        val headLookPacket = ServerEntityHeadLook(
            player.entityID,
            player.location.yaw
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(rotPacket)
            otherPlayer.clientSession.sendPacket(headLookPacket)
        }
    }

    /**
     * Handle of a new position
     */
    private fun handleMove(
        player: Player,
        newLocation: LocationType.Location,
        onGround: Boolean,
    ): Boolean {
        val event = PlayerMoveEvent(
            player,
            newLocation,
            player.location.copy()
        )
        EventManager.fire(event)
        if(event.isCancelled) return false

        val wasOnGround = player.onGround

        val newChunkX = (newLocation.x / 16).toInt()
        val newChunkZ = (newLocation.z / 16).toInt()

        if(newChunkX != player.chunkX || newChunkZ != player.chunkZ) {
            player.chunkX = newChunkX
            player.chunkZ = newChunkZ

            client.sendPacket(
                ServerUpdateViewPositionPacket(
                    newChunkX,
                    newChunkZ
                )
            )
            client.updatePlayerChunks(newChunkX, newChunkZ)
        }
        handleFoodLevel(player, newLocation.x, newLocation.z, onGround, wasOnGround)

        player.location = newLocation
        player.onGround = onGround
        checkFallDamage()

        return true
    }

    /**
     * Handles when a player moves to a new position and rotation axis at the same time
     */
    @PacketReceiver
    fun onPlayerPositionAndRotation(packet: ClientPlayerPositionAndRotation) {
        val newLocation = LocationType.Location(packet.x, packet.feetY, packet.z, packet.yaw, packet.pitch)

        val player = client.player
        val lastLocation = player.location

        if(!handleMove(player, newLocation, packet.onGround)) return

        val (deltaX, deltaY, deltaZ) = calculateDeltas(
            packet.x, packet.feetY, packet.z,
            lastLocation.x, lastLocation.y, lastLocation.z
        )

        val posAndRotPacket = ServerEntityPositionAndRotationPacket(
            player.entityID,
            deltaX,
            deltaY,
            deltaZ,
            player.location.yaw,
            player.location.pitch,
            player.onGround
        )

        val headLookPacket = ServerEntityHeadLook(
            player.entityID,
            player.location.yaw
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(posAndRotPacket)
            otherPlayer.clientSession.sendPacket(headLookPacket)
        }
    }

    /**
     * Handles when a player moves to a new position
     */
    @PacketReceiver
    fun onPlayerPosition(packet: ClientPlayerPositionPacket) {
        val newLocation = client.player.location.set(
            packet.x, packet.feetY, packet.z
        )

        val player = client.player
        val lastLocation = player.location

        if(!handleMove(player, newLocation, packet.onGround)) return

        val (deltaX, deltaY, deltaZ) = calculateDeltas(
            packet.x, packet.feetY, packet.z,
            lastLocation.x, lastLocation.y, lastLocation.z
        )

        val posPacket = ServerEntityPositionPacket(
            player.entityID,
            deltaX,
            deltaY,
            deltaZ,
            player.onGround
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(posPacket)
        }
    }

    /**
     * Handles when a chat message is received
     */
    @PacketReceiver
    fun onChatMessage(packet: ClientChatMessagePacket) {
        val message = packet.message

        if(message.length > 255) {
            client.player.sendMessage(
                Component.text("Message is too long")
                .color(NamedTextColor.RED)
            )

            return
        }

        if(message.startsWith('/') && message.length > 1) {
            val command = message.substring(1)
            val commandSource = client.player

            @Suppress("TooGenericExceptionCaught")
            val result: Int = try {
                CommandManager.dispatcher.execute(command, commandSource)
            } catch (e: CommandSyntaxException){
                CommandCodes.ILLEGAL_SYNTAX.id
            } catch (e: Exception) {
                Bullet.logger.warn("Error running command `$message`:", e)
                return
            }

            if(result == CommandCodes.SUCCESS.id) return

            when(result) {
                CommandCodes.UNKNOWN.id ->
                    commandSource.sendMessage(Component.text("Unknown command")
                        .color(NamedTextColor.RED))

                CommandCodes.ILLEGAL_ARGUMENT.id,
                CommandCodes.ILLEGAL_SYNTAX.id ->
                    commandSource.sendMessage(Component.text("Invalid command syntax, try typing /help")
                        .color(NamedTextColor.RED))

                CommandCodes.INVALID_PERMISSIONS.id ->
                    commandSource.sendMessage(Component.text("You don't have permission to use this command")
                        .color(NamedTextColor.RED))
            }
            return
        }

        val formattedMessage = message.replace('&', '§')

        val event = PlayerChatEvent(client.player, formattedMessage)
        EventManager.fire(event)
        if(event.isCancelled) return

        val textComponent = Component.text()
            .append(Component.text().content("<").color(NamedTextColor.GRAY))
            .append(Component.text().content(client.player.username).color(TextColor.color(0x55FFFF)))
            .append(Component.text().content("> ").color(NamedTextColor.GRAY))
            .append(MiniMessage.miniMessage().deserialize(formattedMessage))
            .build()

        Bullet.broadcast(textComponent)
    }

    /**
     * Handles when the client responds to the server keep alive packet to tell the server the client is still online
     * It also calculates the round trip time (RTT) and updates the players ping
     */
    @PacketReceiver
    fun onKeepAlive(packet: ClientKeepAlivePacket) {
        val event = PlayerHeartbeatEvent(client.player)
        EventManager.fire(event)
        if(event.isCancelled) return

        client.respondedToKeepAlive = true

        val receivedTimestamp = packet.keepAliveID
        val currentTime = System.currentTimeMillis()
        val rtt = (currentTime - receivedTimestamp).toInt()

        client.player.ping = rtt / 2

        for(player in Bullet.players) {
            player.sendPacket(ServerPlayerInfoPacket(
                2,
                client.player
            ))
        }
    }

    /**
     * Handles when the client tells the server it's ready to log in
     *
     * The server first checks for a valid version and uuid, then sends a login success packet
     * It'll then transition the game state into play mode
     * and send a join game and player position/look packet to get past all loading screens
     */
    @PacketReceiver
    fun onLoginStart(packet: ClientLoginStartPacket) {
        val preJoinEvent = PlayerPreJoinEvent()
        EventManager.fire(preJoinEvent)
        if(preJoinEvent.isCancelled) return

        val username = packet.username
        val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:$username").toByteArray())

        checkLoginValidity(username)

        val player = initializePlayer(username, uuid)

        client.sendPacket(ServerLoginSuccessPacket(uuid, username))
        client.state = GameState.PLAY

        if(checkForBan()) return

        client.sendPacket(
            ServerJoinGamePacket(
                player.entityID,
                false,
                player.gameMode,
                "minecraft:overworld",
                Bullet.dimensionCodec!!,
                Bullet.max_players,
                32,
                reducedDebugInfo = false,
                enableRespawnScreen = true,
                isDebug = false,
                isFlat = true
            )
        )

        client.sendPacket(ServerPlayerPositionAndLookPacket(player.location))

        val joinEvent = PlayerJoinEvent(client.player)
        EventManager.fire(joinEvent)
        if(joinEvent.isCancelled) return

        Bullet.players.add(player)
        readPlayerPersistentData()
        scheduleTimers()

        client.sendPacket(ServerChunkPacket(0, 0))
        sendSpawnPlayerPackets(player)

        client.sendPacket(ServerUpdateViewPositionPacket(player.chunkX, player.chunkZ))
        client.updatePlayerChunks(player.chunkX, player.chunkZ)

        sendBlockChanges()
        sendEntities()

        Bullet.storage.storage.writePlayerData(player)

        val world = player.world!!
        player.setTimeOfDay(world.timeOfDay)
        if(world.weather == 1) player.sendPacket(ServerChangeGameStatePacket(2, 0f))
        else player.sendPacket(ServerChangeGameStatePacket(1, 0f))

        val (nodes, rootIndex) = buildCommandGraphFromDispatcher(CommandManager.dispatcher)
        client.sendPacket(ServerDeclareCommandsPacket(nodes, rootIndex))
    }

    /**
     * Handles a ping packet by sending a pong response and closing the connection
     */
    @PacketReceiver
    fun onPing(packet: ClientStatusPingPacket) {
        client.sendPacket(ServerStatusPongPacket(packet.payload))
        client.close()
    }

    /**
     * Handles a status request packet by sending a server status response
     */
    @PacketReceiver
    fun onStatusRequest(packet: ClientStatusRequestPacket) {
        val event = StatusRequestEvent(Bullet.max_players, 0, Bullet.motd)
        EventManager.fire(event)
        if(event.isCancelled) return

        val response = ServerStatusResponse(
            ServerStatusResponse.Version(Bullet.VERSION, Bullet.PROTOCOL),
            ServerStatusResponse.Players(event.maxPlayers, event.onlinePlayers),
            event.motd,
            Bullet.favicon,
            false
        )

        client.sendPacket(ServerStatusResponsePacket(Json.encodeToString(response)))
    }

    /**
     * Handles a handshake packet by updating the client state and protocol
     */
    @PacketReceiver
    fun onHandshake(packet: HandshakePacket) {
        client.state = if(packet.state == 2) GameState.LOGIN else GameState.STATUS
        client.protocol = packet.protocol ?: -1

        val event = HandshakeEvent(client.state, client.protocol)
        EventManager.fire(event)
        if(event.isCancelled) return
    }

    /**
     * Dispatches the given packet to the corresponding handler method based on its type
     *
     * @param packet The packet to handle
     */
    fun handle(packet: Packet) {
        @Suppress("TooGenericExceptionCaught")
        try {
            for(method in javaClass.methods) {
                if(method.isAnnotationPresent(PacketReceiver::class.java)) {
                    val params: Array<Class<*>> = method.parameterTypes
                    if(params.size == 1 && params[0] == packet.javaClass) {
                        method.invoke(this, packet)
                    }
                }
            }
        } catch (e: Exception){
            Bullet.logger.error("Could not handle packet ${packet.javaClass.name}", e)
        }
    }

    private fun calculateDeltas(
        currentX: Double, currentY: Double, currentZ: Double,
        lastX: Double, lastY: Double, lastZ: Double
    ): Triple<Short, Short, Short> {
        val deltaX = ((currentX - lastX) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        val deltaY = ((currentY - lastY) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        val deltaZ = ((currentZ - lastZ) * 4096).toInt().coerceIn(-32768, 32767).toShort()
        return Triple(deltaX, deltaY, deltaZ)
    }

    private fun initializePlayer(username: String, uuid: UUID): Player {
        val player = Player(client)
        player.username = username
        player.uuid = uuid

        for(i in 1..45) {
            player.inventory.items[i] = 0
        }

        player.location = LocationType.Location(8.5, 2.0, 8.5)
        player.onGround = false

        if(player.gameMode != GameMode.SURVIVAL || player.gameMode != GameMode.ADVENTURE) {
            player.canFly = true
        }

        client.player = player
        return player
    }

    private fun sendSpawnPlayerPackets(player: Player) {
        for(otherPlayer in Bullet.players) {
            if(otherPlayer != player) {
                otherPlayer.clientSession.sendPacket(
                    ServerSpawnPlayerPacket(
                        player.entityID,
                        player.uuid,
                        player.location
                    )
                )
            }
        }

        for(existingPlayer in Bullet.players) {
            if(existingPlayer != player) {
                client.sendPacket(
                    ServerSpawnPlayerPacket(
                        existingPlayer.entityID,
                        existingPlayer.uuid,
                        existingPlayer.location
                    )
                )
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startBlockBreak(blockPos: BlockPositionType.BlockPosition, breakTime: Int) {
        if(breakingBlocks.containsKey(blockPos)) return

        val job = GlobalScope.launch {
            val stepTime = breakTime.toLong() / 9

            for(stage in 0..9) {
                for(otherPlayer in Bullet.players) {
                    if(otherPlayer != client.player) {
                        otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(client.player.entityID, blockPos, stage))
                    }
                }

                delay(stepTime)
            }

            for(otherPlayer in Bullet.players) {
                if(otherPlayer != client.player) {
                    otherPlayer.sendPacket(ServerBlockChangePacket(blockPos, 0))
                }
            }

            breakingBlocks.remove(blockPos)
        }

        breakingBlocks[blockPos] = job
    }

    private fun stopBlockBreak(blockPos: BlockPositionType.BlockPosition) {
        breakingBlocks[blockPos]?.cancel()
        breakingBlocks.remove(blockPos)

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(otherPlayer.entityID, blockPos, -1))
            }
        }
    }

    private fun getStoneBreakTime(): Long {
        return ((1.5 * 30) * 140).toLong()
    }

    private fun updateEntityMetadata(player: Player, index: Int, value: Int) {
        val packet = ServerEntityMetadataPacket(
            player.entityID,
            listOf(MetadataType.MetadataEntry(index.toByte(), 18, value))
        )

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != player) {
                otherPlayer.sendPacket(packet)
            }
        }
    }

    private fun sendHeldItemUpdate() {
        val heldItemID = client.player.getHeldItem()

        val heldItemSlot = if(heldItemID == 0) Slot.SlotData(false)
        else Slot.SlotData(true, heldItemID, 1, null)

        for(otherPlayer in Bullet.players) {
            if(otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerEntityEquipmentPacket(
                    client.player.entityID,
                    listOf(0 to heldItemSlot)
                ))
            }
        }
    }

    /**
     * Handles updating the food level when the player moves
     *
     * @param player The player to update
     * @param x The current X position of the player
     * @param z The current Z position of the player
     * @param onGround Whether the player is on the ground
     * @param wasOnGround If the player was on the ground before the movement packet was called
     */
    private fun handleFoodLevel(player: Player, x: Double, z: Double, onGround: Boolean, wasOnGround: Boolean) {
        if(!onGround && wasOnGround) {
            if(sprinting.contains(player.entityID)) {
                player.status.exhaustion += 0.2f
            } else {
                player.status.exhaustion += 0.05f
            }
        }

        if(sprinting.contains(player.entityID)) {
            val distance = sqrt(
                (x - player.lastSprintLocation!!.x).pow(2) +
                    (z - player.lastSprintLocation!!.z).pow(2)
            )

            if(distance >= 1) {
                player.status.exhaustion += 0.1f
                player.lastSprintLocation = player.location
            }
        }
    }

    private fun checkFallDamage() {
        val player = client.player
        if(player.gameMode == GameMode.SURVIVAL) {
            if(player.onGround) {
                if(player.fallDistance > 3) {
                    val damage = ((player.fallDistance - 3).coerceAtLeast(0.0)).toInt()
                    player.status.health -= damage

                    player.sendPacket(ServerUpdateHealthPacket(
                        player.status.health.toFloat(),
                        player.status.foodLevel,
                        player.status.saturation
                    ))
                }

                player.fallDistance = 0.0
                player.lastOnGroundY = player.location.y
            } else {
                if(player.location.y < player.lastOnGroundY) {
                    player.fallDistance += player.lastOnGroundY - player.location.y
                    player.lastOnGroundY = player.location.y
                } else {
                    player.lastOnGroundY = player.location.y
                }
            }
        }
    }

    private fun removeBlock(blockPos: BlockPositionType.BlockPosition) {
        val world = world
        if(world.modifiedBlocks.keys.find {
                it.x == blockPos.x && it.y == blockPos.y && it.z == blockPos.z
            } != null) {
            world.modifiedBlocks.remove(blockPos)
        } else {
            world.modifiedBlocks[blockPos] = BlockWithMetadata(0)
        }

    }

    private fun checkLoginValidity(username: String) {
        if(client.protocol > Bullet.PROTOCOL) {
            client.disconnect(Component.text()
                .append(Component.text("Your client is outdated, please downgrade to minecraft version"))
                .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                .build()
            )

            return
        } else if(client.protocol < Bullet.PROTOCOL) {
            client.disconnect(Component.text()
                .append(Component.text("Your client is outdated, please upgrade to minecraft version"))
                .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                .build()
            )

            return
        }

        if(!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) {
            client.disconnect(Component.text("Invalid username"))
            return
        }
    }

    private fun scheduleTimers() {
        client.sendPlayerSpawnPacket()
        client.scheduleKeepAlive()
        client.scheduleHalfSecondUpdate()

        if(Bullet.shouldPersist) client.scheduleSaving()
    }

    private fun readPlayerPersistentData() {
        val player = client.player

        val data = Bullet.storage.storage.readPlayerData(player.uuid) ?: return

        player.status.health = data.health
        player.status.foodLevel = data.foodLevel
        player.status.saturation = data.saturation
        player.status.exhaustion = data.exhaustionLevel
        player.location = data.location

        player.sendPacket(ServerUpdateHealthPacket(
            player.status.health.toFloat(),
            player.status.foodLevel,
            player.status.saturation
        ))

        player.sendPacket(ServerPlayerPositionAndLookPacket(player.location))
    }

    private fun sendBlockChanges() {
        val blocks = world.modifiedBlocks

        for((position, metadata) in blocks) {
            val block = Block.getBlockFromID(metadata.blockID)
                ?: Item.getItemFromID(metadata.blockID) ?: Block.AIR

            if(block is Block) {
                val state = Block.getStateID(block)
                client.player.sendPacket(ServerBlockChangePacket(position, state))
            } else if(block is Item) {
                val state = Item.getStateID(block)
                client.player.sendPacket(ServerBlockChangePacket(position, state))

                if(block in BlockTags.SIGNS && metadata.textLines != null) {
                    val data = CompoundTag("")
                    data.putString("id", "minecraft:sign")
                    data.putInt("x", position.x.toInt())
                    data.putInt("y", position.y.toInt())
                    data.putInt("z", position.z.toInt())
                    metadata.textLines.forEachIndexed { index, line ->
                        data.putString("Text${index + 1}", "{\"text\":\"$line\"}")
                    }

                    client.sendPacket(ServerBlockEntityDataPacket(
                        position,
                        9,
                        data
                    ))
                }
            }
        }
    }

    private fun checkForBan(): Boolean {
        // Get ban or return true i
        val ban = Bullet.storage.getPlayerBan(client.player.uuid) ?: return false

        val durationMillis = ban.duration.inWholeMilliseconds
        val banEnd = ban.currentTime + durationMillis
        val now = System.currentTimeMillis()

        if(durationMillis > 0 && now >= banEnd) {
            Bullet.storage.unbanPlayer(ban.uuid)
            return false
        }

        val expirationText = if(durationMillis <= 0) {
            "permanently"
        } else {
            val expirationTime = Instant.ofEpochMilli(banEnd)
                .atZone(ZoneId.systemDefault())

            val dayOfMonth = expirationTime.dayOfMonth
            val daySuffix = DurationFormat.getDaySuffix(dayOfMonth)

            val formattedDate = expirationTime.format(
                DateTimeFormatter.ofPattern("MMMM d'$daySuffix' yyyy 'at' H:mm")
            )

            "Expires $formattedDate"
        }

        client.disconnect(
            Component.text()
                .append(Component.text("You have been banned!\n", NamedTextColor.RED))
                .append(Component.text(expirationText, NamedTextColor.RED))
                .append(Component.text("\n\n", NamedTextColor.RED))
                .append(Component.text("Reason: ", NamedTextColor.RED))
                .append(Component.text(ban.reason, NamedTextColor.GRAY))
                .build()
        )

        return true
    }

    private fun handlePlacement(block: Any, event: BlockPlaceEvent, heldItem: Int) {
        try {
            val stateID = when (block) {
                is Block -> Block.getStateID(block)
                is Item -> try {
                    Item.getStateID(block)
                } catch(e: IllegalArgumentException) {
                    -1
                }
                else -> throw IllegalArgumentException("Unknown block or item type")
            }


            for(otherPlayer in Bullet.players) {
                if(otherPlayer != client.player && stateID != -1) {
                    otherPlayer.sendPacket(
                        ServerBlockChangePacket(
                            event.blockPos.copy(),
                            stateID
                        )
                    )
                }
            }

            when {
                block is Item && block in BlockTags.SIGNS -> {
                    client.sendPacket(ServerOpenSignEditorPacket(event.blockPos))
                    world.modifiedBlocks[event.blockPos] = BlockWithMetadata(heldItem, listOf("", "", "", ""))
                }

                block is Item && block in BlockTags.SPAWN_EGGS -> {
                    handleSpawnEgg(block, event)
                }

                else -> {
                    world.modifiedBlocks[event.blockPos] = BlockWithMetadata(heldItem)
                }
            }
        } catch(e: IllegalArgumentException) {
            //do nothing
        }
    }

    private fun sendEntities() {
        for(entity in world.entities) {
            client.sendPacket(ServerSpawnEntityPacket(
                entity.first.entityID,
                entity.second.uuid,
                entity.second.entityType,
                entity.second.location,
                entity.second.velocityX,
                entity.second.velocityY,
                entity.second.velocityZ,
            ))
        }

        for(livingEntity in world.livingEntities) {
            client.sendPacket(ServerSpawnLivingEntityPacket(
                livingEntity.first.entityID,
                livingEntity.second.uuid,
                livingEntity.second.entityType,
                livingEntity.second.location,
                livingEntity.second.headPitch,
                livingEntity.second.velocityX,
                livingEntity.second.velocityY,
                livingEntity.second.velocityZ,
            ))
        }
    }

    private fun handleSpawnEgg(block: Item, event: BlockPlaceEvent) {
        val itemName = block.name.removeSuffix("_SPAWN_EGG")
        val entity = LivingEntities.entries.find { it.name.equals(itemName, true) }

        if(entity != null) {
            val location = LocationType.Location(
                event.blockPos.x + 0.5,
                event.blockPos.y,
                event.blockPos.z + 0.5
            )

            val newEntity = LivingEntity()
            val entityType = entity.id

            client.sendPacket(
                ServerSpawnLivingEntityPacket(
                    newEntity.entityID,
                    newEntity.uuid,
                    entityType,
                    location,
                    90f,
                    0,
                    0,
                    0
                )
            )

            world.livingEntities.add(
                Pair(
                    newEntity, EntityData(
                        newEntity.uuid,
                        location,
                        entityType,
                        20,
                        90f,
                        0,
                        0,
                        0,
                    )
                )
            )
        }
    }

    private fun sendBlockBreakParticles(player: Player, block: Int, blockPos: BlockPositionType.BlockPosition) {
        player.sendPacket(ServerParticlePacket(
            Particles.Block(block),
            false,
            blockPos.add(0.5, 0.5, 0.5),
            0.2f,
            0.22f,
            0.2f,
            0f,
            25
        ))
    }
}
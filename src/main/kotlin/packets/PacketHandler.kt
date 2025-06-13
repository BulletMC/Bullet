package com.aznos.packets

import com.aznos.Bullet
import com.aznos.Bullet.breakingBlocks
import com.aznos.Bullet.players
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
import com.aznos.datatypes.Slot.toItemStack
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.DroppedItem
import com.aznos.entity.Entity
import com.aznos.entity.OrbEntity
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.nonliving.Entities
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.PlayerProfile
import com.aznos.events.*
import com.aznos.packets.data.ServerStatusResponse
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.`in`.ClientEncryptionResponsePacket
import com.aznos.packets.login.out.ServerEncryptionRequestPacket
import com.aznos.packets.login.out.ServerLoginDisconnectPacket
import com.aznos.packets.login.out.ServerLoginSuccessPacket
import com.aznos.packets.play.`in`.*
import com.aznos.packets.play.`in`.movement.*
import com.aznos.packets.play.out.*
import com.aznos.packets.play.out.movement.*
import com.aznos.packets.play.out.ServerCollectItemPacket
import com.aznos.packets.play.out.ServerSpawnExperienceOrb
import com.aznos.packets.play.out.ServerSetExperiencePacket
import com.aznos.packets.status.`in`.ClientStatusPingPacket
import com.aznos.packets.status.`in`.ClientStatusRequestPacket
import com.aznos.packets.status.out.ServerStatusPongPacket
import com.aznos.world.blocks.Block
import com.aznos.util.DurationFormat
import com.aznos.util.Hashes
import com.aznos.util.MojangNetworking
import com.aznos.world.World
import com.aznos.world.blocks.BlockTags
import com.aznos.world.data.Axis
import com.aznos.world.data.BlockStatus
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.Particles
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.sun.source.doctree.EntityTree
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.querz.nbt.tag.CompoundTag
import packets.handshake.HandshakePacket
import packets.status.out.ServerStatusResponsePacket
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.security.SecureRandom
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.compareTo
import kotlin.experimental.and
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

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
    fun onWindowClick(packet: ClientClickWindowPacket) {
        if (packet.windowID.toInt() == 0 && packet.mode == 4) {
            val player = client.player
            val held = player.inventory.heldStack(player.selectedSlot)
            if (held.isAir) return

            val dropAll = (packet.button.toInt() == 1)
            val toDrop = if (dropAll) held else held.copy(count = 1)

            if (dropAll) {
                player.inventory.setHeldSlot(player.selectedSlot, null)
            } else {
                val newCount = held.count - 1
                if (newCount <= 0) player.inventory.setHeldSlot(player.selectedSlot, null)
                else player.inventory.setHeldSlot(player.selectedSlot, held.copy(count = newCount))
            }

            client.sendPacket(
                ServerSetSlotPacket(
                    0, player.selectedSlot + 36,
                    player.inventory.heldStack(player.selectedSlot).toSlotData()
                )
            )

            client.sendPacket(
                ServerWindowConfirmationPacket(
                    0, packet.actionNumber, true
                )
            )

            dropItem(player.location.toBlockPosition(), toDrop.id)
        }
    }

    @PacketReceiver
    fun onUpdateSign(packet: ClientUpdateSignPacket) {
        val data = CompoundTag()
        data.putString("id", "minecraft:sign")
        data.putInt("x", packet.blockPos.x.toInt())
        data.putInt("y", packet.blockPos.y.toInt())
        data.putInt("z", packet.blockPos.z.toInt())

        data.putString("Text1", "{\"text\":\"${packet.line1}\"}")
        data.putString("Text2", "{\"text\":\"${packet.line2}\"}")
        data.putString("Text3", "{\"text\":\"${packet.line3}\"}")
        data.putString("Text4", "{\"text\":\"${packet.line4}\"}")

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != client.player) {
                otherPlayer.sendPacket(
                    ServerBlockEntityDataPacket(
                        packet.blockPos,
                        9,
                        data
                    )
                )
            }
        }

        val world = world
        val prev = world.modifiedBlocks[packet.blockPos]
        if (prev != null) {
            val lines = listOf(packet.line1, packet.line2, packet.line3, packet.line4)
            world.modifiedBlocks[packet.blockPos] = prev.copy(textLines = lines)
        }
    }

    @PacketReceiver
    fun onPlayerAbilities(packet: ClientPlayerAbilitiesPacket) {
        if (client.player.canFly) {
            val flying = (packet.flags and 0x02).toInt() == 0x02
            client.player.isFlying = flying
        } else {
            client.player.isFlying = false
            client.player.sendPacket(
                ServerPlayerAbilitiesPacket(
                    0,
                    0f,
                )
            )
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

            client.player.sendPacket(
                ServerTabCompletePacket(
                    packet.transactionID,
                    start = start + 1,
                    length = length,
                    matches = formattedMatches
                )
            )
        }
    }

    @PacketReceiver
    fun onClientStatus(packet: ClientStatusPacket) {
        when (packet.actionID) {
            0 -> { // Perform respawn
                client.player.sendPacket(
                    ServerRespawnPacket(
                        Bullet.dimensionCodec!!,
                        "minecraft:overworld",
                        GameMode.SURVIVAL,
                        false,
                        false,
                        true
                    )
                )

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
        if (event.isCancelled) return

        if (packet.type == 1) {
            for (player in Bullet.players) {
                if (player.entityID == packet.entityID && player.gameMode == GameMode.SURVIVAL) {
                    player.status.health -= 1

                    player.sendPacket(
                        ServerUpdateHealthPacket(
                            player.status.health.toFloat(),
                            player.status.foodLevel,
                            player.status.saturation
                        )
                    )

                    player.sendPacket(
                        ServerAnimationPacket(
                            player.entityID,
                            1
                        )
                    )

                    player.status.exhaustion += 0.1f

                    val dx = player.location.x - attacker.location.x
                    val dy = player.location.y - attacker.location.y
                    val dz = player.location.z - attacker.location.z
                    val distance = sqrt(dx * dx + dy * dy + dz * dz)
                    if (distance != 0.0) {
                        val kbStrength = 0.5

                        val kbX = (dx / distance) * kbStrength
                        val kbY = if (player.onGround) 0.3 else 0.125
                        val kbZ = (dz / distance) * kbStrength

                        player.sendPacket(
                            ServerEntityVelocityPacket(
                                player.entityID,
                                (kbX * 8000).toInt().toShort(),
                                (kbY * 8000).toInt().toShort(),
                                (kbZ * 8000).toInt().toShort()
                            )
                        )
                    }
                }
            }
        }
    }

    @PacketReceiver
    fun onHeldItemChange(packet: ClientHeldItemChangePacket) {
        val event = PlayerHeldItemChangeEvent(client.player, packet.slot.toInt())
        EventManager.fire(event)
        if (event.isCancelled) return

        client.player.selectedSlot = packet.slot.toInt()
        sendHeldItemUpdate()
    }

    @PacketReceiver
    fun onCreativeInventoryAction(packet: ClientCreativeInventoryActionPacket) {
        val slotIdx = packet.slotIndex.toInt()
        val stack = if (packet.slot.present) packet.slot.toItemStack() else null
        client.player.inventory.set(slotIdx, stack)

        if (slotIdx == client.player.selectedSlot + 36) sendHeldItemUpdate()
        if (slotIdx == -1) { //drop item
            if (stack != null && !stack.isAir) {
                val vx = ((Math.random() - 0.5) * 0.2 * 8000).toInt().toShort()
                val vy = (0.1 * 8000).toInt().toShort()
                val vz = ((Math.random() - 0.5) * 0.2 * 8000).toInt().toShort()

                dropItem(client.player.location.toBlockPosition(), stack.id, vx, vy, vz)
            }
        }
    }

    @PacketReceiver
    fun onPluginMessage(packet: ClientPluginMessagePacket) {
        when (packet.channel) {
            "minecraft:brand" -> {
                val input = DataInputStream(ByteArrayInputStream(packet.pluginData))
                val length = input.readVarInt()

                val brandBytes = ByteArray(length)
                input.readFully(brandBytes)

                val brand = String(brandBytes, Charsets.UTF_8)
                client.player.brand = brand

                val event = PlayerBrandEvent(client.player, brand)
                EventManager.fire(event)
                if (event.isCancelled) {
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
        if (event.isCancelled) return

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
        when (packet.actionID) {
            0 -> { //Start sneaking
                val event = PlayerSneakEvent(client.player, true)
                EventManager.fire(event)
                if (event.isCancelled) return

                client.player.isSneaking = true
                updateEntityMetadata(client.player, 6, 5)
            }

            1 -> { //Stop sneaking
                val event = PlayerSneakEvent(client.player, false)
                EventManager.fire(event)
                if (event.isCancelled) return

                client.player.isSneaking = false
                updateEntityMetadata(client.player, 6, 0)
            }

            2 -> { //Leave bed
                handleWakeUp(client.player)
            }

            3 -> { //Start sprinting
                val event = PlayerSprintEvent(client.player, true)
                EventManager.fire(event)
                if (event.isCancelled) return

                sprinting.add(client.player.entityID)
                client.player.lastSprintLocation = client.player.location
            }

            4 -> { //Stop sprinting
                val event = PlayerSprintEvent(client.player, false)
                EventManager.fire(event)
                if (event.isCancelled) return

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
        if (event.isCancelled) return

        if (client.player.gameMode == GameMode.CREATIVE && event.status == BlockStatus.STARTED_DIGGING.id) {
            for (otherPlayer in Bullet.players) {
                if (otherPlayer != client.player) {
                    otherPlayer.sendPacket(
                        ServerBlockChangePacket(
                            event.blockPos,
                            0
                        )
                    )

                    val block = world.modifiedBlocks[event.blockPos]?.stateID ?: Block.GRASS_BLOCK.id
                    sendBlockBreakParticles(otherPlayer, block, event.blockPos)
                }
            }

            removeBlock(event.blockPos)
        } else if (client.player.gameMode == GameMode.SURVIVAL) {
            when (event.status) {
                BlockStatus.STARTED_DIGGING.id -> {
                    val breakTime = getBlockBreakTime(
                        world.modifiedBlocks[event.blockPos]?.stateID ?: Block.GRASS_BLOCK
                    )
                    startBlockBreak(event.blockPos, breakTime.toInt())
                }

                BlockStatus.CANCELLED_DIGGING.id -> {
                    stopBlockBreak(event.blockPos)
                }

                BlockStatus.FINISHED_DIGGING.id -> {
                    client.player.status.exhaustion += 0.005f
                    val block = world.modifiedBlocks[event.blockPos]?.blockID ?: Block.GRASS_BLOCK.id

                    val vx = ((Math.random() - 0.5) * 0.1 * 8000).toInt().toShort()
                    val vy = (0.1 * 8000).toInt().toShort()
                    val vz = ((Math.random() - 0.5) * 0.1 * 8000).toInt().toShort()

                    val itemID = world.modifiedBlocks[event.blockPos]?.blockID
                    dropItem(event.blockPos, itemID ?: 0, vx, vy, vz)

                    stopBlockBreak(event.blockPos)
                    sendBlockBreakParticles(client.player, block, event.blockPos)
                    removeBlock(event.blockPos)
                }
            }
        }

        when (event.status) {
            BlockStatus.DROP_ITEM.id, BlockStatus.DROP_ITEM_STACK.id -> {
                handleBlockDrop(event.blockPos, event.status)
            }
        }
    }

    @PacketReceiver
    fun onArmSwing(packet: ClientAnimationPacket) {
        val event = PlayerArmSwingEvent(client.player)
        EventManager.fire(event)
        if (event.isCancelled) return

        if (client.player.getHeldItemID() == Item.EXPERIENCE_BOTTLE.id) {
            world.orbs.add(OrbEntity())
            val orb = world.orbs.last()
            orb.location = client.player.location.copy().add(0.0, 1.0, 0.0)
            orb.xp = (3..11).random()

            for (player in Bullet.players) {
                player.sendPacket(
                    ServerSpawnExperienceOrb(
                        orb.entityID,
                        client.player.location.toBlockPosition().add(0.0, 1.0, 0.0),
                        orb.xp
                    )
                )

                player.sendPacket(
                    ServerSoundEffectPacket(
                        Sounds.ENTITY_EXPERIENCE_BOTTLE_THROW,
                        SoundCategories.PLAYER,
                        client.player.location.x.toInt(),
                        client.player.location.y.toInt(),
                        client.player.location.z.toInt()
                    )
                )
            }
        }

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerAnimationPacket(client.player.entityID, 0))
            }
        }
    }

    /**
     * Called when a client places a block
     */
    @PacketReceiver
    fun onBlockPlacement(packet: ClientBlockPlacementPacket) {
        val heldStack = client.player.getHeldItem()
        if(heldStack.isAir || heldStack.count <= 0) return

        val heldItem = client.player.getHeldItemID()

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
        if (event.isCancelled) return

        when (event.face) {
            0 -> event.blockPos.y -= 1
            1 -> event.blockPos.y += 1
            2 -> event.blockPos.z -= 1
            3 -> event.blockPos.z += 1
            4 -> event.blockPos.x -= 1
            5 -> event.blockPos.x += 1
        }

        val block = Block.getBlockFromID(heldItem) ?: Item.getItemFromID(heldItem) ?: Block.AIR
        handlePlacement(block, event, packet.blockPos)
    }

    /**
     * Every 20 ticks the client will send an empty movement packet telling the server if the
     * client is on the ground or not
     */
    @PacketReceiver
    fun onPlayerMovement(packet: ClientPlayerMovement) {
        val player = client.player
        player.onGround = packet.onGround

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != player) continue
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
        if (event.isCancelled) return

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

        for (otherPlayer in Bullet.players) {
            if (otherPlayer == player) continue

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
        if (event.isCancelled) return false

        val wasOnGround = player.onGround

        val newChunkX = (newLocation.x / 16).toInt()
        val newChunkZ = (newLocation.z / 16).toInt()

        if (newChunkX != player.chunkX || newChunkZ != player.chunkZ) {
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
        checkOrbs()
        checkItems()

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

        if (!handleMove(player, newLocation, packet.onGround)) return

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

        for (otherPlayer in Bullet.players) {
            if (otherPlayer == player) continue

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

        if (!handleMove(player, newLocation, packet.onGround)) return

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

        for (otherPlayer in Bullet.players) {
            if (otherPlayer == player) continue

            otherPlayer.clientSession.sendPacket(posPacket)
        }
    }

    /**
     * Handles when a chat message is received
     */
    @PacketReceiver
    fun onChatMessage(packet: ClientChatMessagePacket) {
        val message = packet.message

        if (message.length > 255) {
            client.player.sendMessage(
                Component.text("Message is too long")
                    .color(NamedTextColor.RED)
            )

            return
        }

        if (message.startsWith('/') && message.length > 1) {
            val command = message.substring(1)
            val commandSource = client.player

            @Suppress("TooGenericExceptionCaught")
            val result: Int = try {
                CommandManager.dispatcher.execute(command, commandSource)
            } catch (e: CommandSyntaxException) {
                CommandCodes.ILLEGAL_SYNTAX.id
            } catch (e: Exception) {
                Bullet.logger.warn("Error running command `$message`:", e)
                return
            }

            if (result == CommandCodes.SUCCESS.id) return

            when (result) {
                CommandCodes.UNKNOWN.id ->
                    commandSource.sendMessage(
                        Component.text("Unknown command")
                            .color(NamedTextColor.RED)
                    )

                CommandCodes.ILLEGAL_ARGUMENT.id,
                CommandCodes.ILLEGAL_SYNTAX.id ->
                    commandSource.sendMessage(
                        Component.text("Invalid command syntax, try typing /help")
                            .color(NamedTextColor.RED)
                    )

                CommandCodes.INVALID_PERMISSIONS.id ->
                    commandSource.sendMessage(
                        Component.text("You don't have permission to use this command")
                            .color(NamedTextColor.RED)
                    )
            }
            return
        }

        val formattedMessage = message.replace('&', '§')

        val event = PlayerChatEvent(client.player, formattedMessage)
        EventManager.fire(event)
        if (event.isCancelled) return

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
        if (event.isCancelled) return

        client.respondedToKeepAlive = true

        val receivedTimestamp = packet.keepAliveID
        val currentTime = System.currentTimeMillis()
        val rtt = (currentTime - receivedTimestamp).toInt()

        client.player.ping = rtt / 2

        for (player in Bullet.players) {
            player.sendPacket(
                ServerPlayerInfoPacket(
                    2,
                    client.player.uuid,
                    ping = client.player.ping
                )
            )
        }
    }

    @PacketReceiver
    fun onEncryptionResponse(packet: ClientEncryptionResponsePacket) {
        val rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        rsa.init(Cipher.DECRYPT_MODE, Bullet.keyPair.private)

        val sharedSecret = rsa.doFinal(packet.secretKey)
        val verifyToken = rsa.doFinal(packet.verifyToken)

        verifyPlayerToken(verifyToken)

        val secretKey = SecretKeySpec(sharedSecret, "AES")
        val iv = IvParameterSpec(sharedSecret)

        val decrypt = Cipher.getInstance("AES/CFB8/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, secretKey, iv)
        }
        val encrypt = Cipher.getInstance("AES/CFB8/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, secretKey, iv)
        }

        client.enableEncryption(decrypt, encrypt)

        val player = client.player

        val hash = Hashes.makeServerIDHash(sharedSecret, Bullet.publicKey)
        val prof = runBlocking { MojangNetworking.querySessionServer(player, hash) }

        if (prof == null) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text(
                        "Failed to verify username with Mojang servers, please try again later",
                        NamedTextColor.RED
                    )
                )
            )

            client.close()
            return
        }

        val uuidWithDashes = prof.id.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)".toRegex(),
            "$1-$2-$3-$4-$5"
        )

        client.player.uuid = UUID.fromString(uuidWithDashes)
        client.player.username = prof.name

        val dupes = players.filter { it.uuid == client.player.uuid || it.username == client.player.username }
        players.removeAll(dupes)

        for (old in dupes) {
            old.disconnect(
                Component.text()
                    .append(Component.text("You logged in from another location", NamedTextColor.RED))
                    .append(
                        Component.text(
                            "\n\nIf this wasn’t you, your account may have been compromised.",
                            NamedTextColor.GRAY
                        )
                    )
                    .build()
            )

            old.clientSession.close()
        }

        loginPlayer()
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
        if (preJoinEvent.isCancelled) return

        val username = packet.username
        val uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:$username").toByteArray())

        checkLoginValidity(username)

        val player = initializePlayer(username, uuid)
        if (!Bullet.onlineMode) {
            val dupes = players.filter {
                it.username == username || it.uuid == uuid
            }

            players.removeAll(dupes)
            dupes.forEach { old ->
                old.sendPacket(
                    ServerLoginDisconnectPacket(
                        Component.text("You are already logged in from another location", NamedTextColor.RED)
                    )
                )

                old.clientSession.close()
            }

            players.add(player)
            loginPlayer()
        }

        handleOnlineModeJoin(packet)
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
        if (event.isCancelled) return

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
        client.state = if (packet.state == 2) GameState.LOGIN else GameState.STATUS
        client.protocol = packet.protocol ?: -1

        val event = HandshakeEvent(client.state, client.protocol)
        EventManager.fire(event)
        if (event.isCancelled) return
    }

    /**
     * Dispatches the given packet to the corresponding handler method based on its type
     *
     * @param packet The packet to handle
     */
    fun handle(packet: Packet) {
        @Suppress("TooGenericExceptionCaught")
        try {
            for (method in javaClass.methods) {
                if (method.isAnnotationPresent(PacketReceiver::class.java)) {
                    val params: Array<Class<*>> = method.parameterTypes
                    if (params.size == 1 && params[0] == packet.javaClass) {
                        method.invoke(this, packet)
                    }
                }
            }
        } catch (e: Exception) {
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

        player.location = LocationType.Location(8.5, 2.0, 8.5)
        player.onGround = false

        if (player.gameMode != GameMode.SURVIVAL || player.gameMode != GameMode.ADVENTURE) {
            player.canFly = true
        }

        client.player = player
        return player
    }

    private fun sendSpawnPlayerPackets(player: Player) {
        for (otherPlayer in Bullet.players) {
            if (otherPlayer != player) {
                otherPlayer.clientSession.sendPacket(
                    ServerSpawnPlayerPacket(
                        player.entityID,
                        player.uuid,
                        player.location
                    )
                )
            }
        }

        for (existingPlayer in Bullet.players) {
            if (existingPlayer != player) {
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
        if (breakingBlocks.containsKey(blockPos)) return

        val job = GlobalScope.launch {
            val stepTime = breakTime.toLong() / 9

            for (stage in 0..9) {
                for (otherPlayer in Bullet.players) {
                    if (otherPlayer != client.player) {
                        otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(client.player.entityID, blockPos, stage))
                    }
                }

                delay(stepTime)
            }

            for (otherPlayer in Bullet.players) {
                if (otherPlayer != client.player) {
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

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != client.player) {
                otherPlayer.sendPacket(ServerBlockBreakAnimationPacket(otherPlayer.entityID, blockPos, -1))
            }
        }
    }

    private fun getBlockBreakTime(block: Any): Long {
        val player = client.player
        val heldItem = player.getHeldItem().item
        val blockObj = when (block) {
            is Block -> block
            is Item -> Block.getBlockFromID(block.id) ?: return 1
            else -> return 0
        }

        val hardness = blockObj.hardness
        if (hardness <= 0) return 0

        val isBestTool = getBestTool(blockObj, heldItem)
        val canHarvest = canHarvestBlock(blockObj, heldItem)
        val toolMultiplier = getToolMultiplier(heldItem)

        var speedMultiplier = if (isBestTool) toolMultiplier else 1.0
        if (!player.onGround) {
            speedMultiplier /= 5.0
        }

        var damage = speedMultiplier / hardness
        damage /= if (canHarvest) {
            30.0
        } else {
            100.0
        }

        if (damage > 1.0) return 0
        val ticks = ceil(1.0 / damage)
        val seconds = ticks / 20.0

        return seconds.toLong()
    }

    private fun updateEntityMetadata(player: Player, index: Int, value: Int) {
        val packet = ServerEntityMetadataPacket(
            player.entityID,
            listOf(MetadataType.MetadataEntry(index.toByte(), 18, value))
        )

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != player) {
                otherPlayer.sendPacket(packet)
            }
        }
    }

    private fun sendHeldItemUpdate() {
        val stack = client.player.getHeldItem()
        val slotData = stack.toSlotData()

        for (otherPlayer in Bullet.players) {
            if (otherPlayer != client.player) {
                otherPlayer.sendPacket(
                    ServerEntityEquipmentPacket(
                        client.player.entityID,
                        listOf(0 to slotData)
                    )
                )
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
        if (!onGround && wasOnGround) {
            if (sprinting.contains(player.entityID)) {
                player.status.exhaustion += 0.2f
            } else {
                player.status.exhaustion += 0.05f
            }
        }

        if (sprinting.contains(player.entityID)) {
            val distance = sqrt(
                (x - player.lastSprintLocation!!.x).pow(2) +
                        (z - player.lastSprintLocation!!.z).pow(2)
            )

            if (distance >= 1) {
                player.status.exhaustion += 0.1f
                player.lastSprintLocation = player.location
            }
        }
    }

    private fun checkFallDamage() {
        val player = client.player
        if (player.gameMode == GameMode.SURVIVAL) {
            if (player.onGround) {
                if (player.fallDistance > 3) {
                    val damage = ((player.fallDistance - 3).coerceAtLeast(0.0)).toInt()
                    player.status.health -= damage

                    player.sendPacket(
                        ServerUpdateHealthPacket(
                            player.status.health.toFloat(),
                            player.status.foodLevel,
                            player.status.saturation
                        )
                    )
                }

                player.fallDistance = 0.0
                player.lastOnGroundY = player.location.y
            } else {
                if (player.location.y < player.lastOnGroundY) {
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
        if (world.modifiedBlocks.keys.find {
                it.x == blockPos.x && it.y == blockPos.y && it.z == blockPos.z
            } != null) {
            world.modifiedBlocks.remove(blockPos)
        } else {
            world.modifiedBlocks[blockPos] = BlockWithMetadata(0, 0)
        }

    }

    private fun checkLoginValidity(username: String): Boolean {
        if (client.protocol > Bullet.PROTOCOL) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text()
                        .append(Component.text("Your client is outdated, please downgrade to minecraft version"))
                        .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                        .build()
                )
            )

            client.close()
            return false
        } else if (client.protocol < Bullet.PROTOCOL) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component.text()
                        .append(Component.text("Your client is outdated, please upgrade to minecraft version"))
                        .append(Component.text(" " + Bullet.VERSION).color(NamedTextColor.GOLD))
                        .build()
                )
            )

            return false
        }

        if (!username.matches(Regex("^[a-zA-Z0-9]{3,16}$"))) {
            client.sendPacket(ServerLoginDisconnectPacket(Component.text("Invalid username")))
            return false
        }

        return true
    }

    private fun scheduleTimers() {
        client.sendPlayerSpawnPacket()
        client.scheduleKeepAlive()
        client.scheduleHalfSecondUpdate()

        if (Bullet.shouldPersist) client.scheduleSaving()
    }

    private fun readPlayerPersistentData() {
        val player = client.player
        val data = Bullet.storage.storage.readPlayerData(player.uuid) ?: return
        player.status.health = data.health
        player.status.foodLevel = data.foodLevel
        player.status.saturation = data.saturation
        player.status.exhaustion = data.exhaustionLevel
        player.location = data.location
        player.permissionLevel = data.permissionLevel
        player.totalXP = data.totalXP

        player.setGameMode(GameMode.entries.find { it.id == data.gameMode } ?: GameMode.SURVIVAL)

        val savedStacks: Map<Int, ItemStack> =
            data.inventory.associate { it.first to it.second }
        player.inventory.clear()

        val totalSlots = 45
        val slotDataList = (0 until totalSlots).map { idx ->
            val stack = savedStacks[idx]
            player.inventory.set(idx, stack)
            stack?.toSlotData() ?: Slot.SlotData(false)
        }

        player.sendPacket(ServerWindowItemsPacket(0, slotDataList))
        sendHeldItemUpdate()
        calculateXPLevels(player.totalXP)

        player.sendPacket(
            ServerUpdateHealthPacket(
                player.status.health.toFloat(),
                player.status.foodLevel,
                player.status.saturation
            )
        )

        player.sendPacket(ServerPlayerPositionAndLookPacket(player.location))

    }

    private fun sendBlockChanges() {
        if (world.modifiedBlocks.isEmpty()) return
        for ((pos, meta) in world.modifiedBlocks) {
            client.player.sendPacket(
                ServerBlockChangePacket(
                    pos, meta.stateID
                )
            )

            if (meta.textLines != null) {
                val nbt = CompoundTag()
                nbt.putString("id", "minecraft:sign")
                nbt.putInt("x", pos.x.toInt())
                nbt.putInt("y", pos.y.toInt())
                nbt.putInt("z", pos.z.toInt())

                meta.textLines.forEachIndexed { idx, line ->
                    nbt.putString("Text${idx + 1}", "{\"text\":\"$line\"}")
                }

                client.player.sendPacket(
                    ServerBlockEntityDataPacket(
                        pos, 9, nbt
                    )
                )
            }
        }
    }

    private fun checkForBan(): Boolean {
        // Get ban or return true i
        val ban = Bullet.storage.getPlayerBan(client.player.uuid) ?: return false

        val durationMillis = ban.duration.inWholeMilliseconds
        val banEnd = ban.currentTime + durationMillis
        val now = System.currentTimeMillis()

        if (durationMillis > 0 && now >= banEnd) {
            Bullet.storage.unbanPlayer(ban.uuid)
            return false
        }

        val expirationText = if (durationMillis <= 0) {
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

    private fun handlePlacement(block: Any, event: BlockPlaceEvent, blockPos: BlockPositionType.BlockPosition) {
        if(block is Item && block == Item.AIR) return
        if(block is Block && block == Block.AIR) return

        try {
            val dir = getCardinalDirection(client.player.location.yaw)
            val properties = modifyBlockProperties(block, dir, event)

            val stateID = getStateID(block, properties)
            if (stateID == -1) return

            val clickedStateID = world.modifiedBlocks[blockPos]?.stateID ?: 0
            val clickedItemID = clickedStateID.let { Item.getIDFromState(it) }

            for (bed in BlockTags.BEDS) {
                if (clickedItemID == bed.id) {
                    handleBedClick(blockPos)
                    break
                }
            }

            players.forEach {
                it.sendPacket(ServerBlockChangePacket(event.blockPos, stateID))

                val entry = when {
                    block is Item && block in BlockTags.SIGNS -> {
                        client.sendPacket(ServerOpenSignEditorPacket(event.blockPos))
                        BlockWithMetadata(block.id, stateID, listOf("", "", "", ""))
                    }

                    block is Item && block in BlockTags.SPAWN_EGGS -> {
                        handleSpawnEgg(block, event)
                        BlockWithMetadata(block.id, stateID)
                    }

                    block is Block -> {
                        BlockWithMetadata(block.id, stateID)
                    }

                    else -> BlockWithMetadata(0, stateID)
                }

                world.modifiedBlocks[event.blockPos] = entry
            }

            if (client.player.gameMode == GameMode.SURVIVAL) {
                val slot = client.player.selectedSlot
                val inv = client.player.inventory
                val held = inv.heldStack(slot)

                if(held.count > 1) {
                    held.count--
                    client.sendPacket(ServerSetSlotPacket(0, slot + 36, held.toSlotData()))
                } else {
                    inv.setHeldSlot(slot, null)
                    client.sendPacket(ServerSetSlotPacket(0, slot + 36, Slot.SlotData(false)))
                }
            }
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    private fun getStateID(block: Any, properties: Map<String, String>): Int {
        return when (block) {
            is Block -> Block.getStateID(block, properties)
            is Item -> Item.getStateID(block, properties)
            else -> -1
        }
    }

    private fun modifyBlockProperties(
        block: Any,
        cardinalDirection: String,
        event: BlockPlaceEvent
    ): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for (furnace in BlockTags.FURNANCES) {
            if (block == furnace) {
                properties["facing"] = cardinalDirection
                properties["lit"] = "false"

                if (block == Block.CAMPFIRE) {
                    properties["waterlogged"] = "false"
                    properties["signal_fire"] = "false"
                }
            }
        }

        if (block == Block.END_ROD) {
            properties["facing"] = cardinalDirection
        }

        if (block == Block.GRINDSTONE) {
            properties["facing"] = cardinalDirection
            properties["face"] = "floor"
        }

        if (block == BlockTags.BANNERS || block == BlockTags.SKULLS) {
            properties["rotation"] = getRotationalDirection(client.player.location.yaw).toString()
        }

        if (block == BlockTags.SIGNS) {
            properties["rotation"] = getRotationalDirection(client.player.location.yaw).toString()
            properties["waterlogged"] = "false"
        }

        modifyStairProperties(block, cardinalDirection).forEach { (key, value) ->
            properties[key] = value
        }

        modifyRedstoneBlockProperties(block, cardinalDirection).forEach { (key, value) ->
            properties[key] = value
        }

        modifyAxisAlignedBlocks(block).forEach { (key, value) ->
            properties[key] = value
        }

        if (block is Item) {
            modifyBedBlocks(block, cardinalDirection, event).forEach { (key, value) ->
                properties[key] = value
            }
        }

        return properties
    }

    private fun modifyStairProperties(block: Any, cardinalDirection: String): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for (stair in BlockTags.STAIRS) {
            if (block == stair) {
                properties["facing"] = cardinalDirection
                properties["half"] = "bottom"
                properties["shape"] = "straight"
                properties["waterlogged"] = "false"
            }
        }

        return properties
    }

    private fun modifyRedstoneBlockProperties(block: Any, cardinalDirection: String): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        if (block == Block.DISPENSER || block == Block.DROPPER) {
            properties["facing"] = cardinalDirection
            properties["triggered"] = "false"
        }

        if (block == Block.PISTON) {
            properties["extended"] = "false"
            properties["facing"] = cardinalDirection
        }

        if (block == Block.OBSERVER) {
            properties["facing"] = cardinalDirection
            properties["powered"] = "false"
        }

        if (block == Block.REPEATER) {
            properties["facing"] = cardinalDirection
            properties["delay"] = "1"
            properties["locked"] = "false"
            properties["powered"] = "false"
        }

        if (block == Block.COMPARATOR) {
            properties["facing"] = cardinalDirection
            properties["mode"] = "compare"
            properties["powered"] = "false"
        }

        if (block == Block.BARREL) {
            properties["facing"] = cardinalDirection
            properties["open"] = "false"
        }

        if (block == Block.LECTERN) {
            properties["facing"] = cardinalDirection
            properties["has_book"] = "false"
            properties["powered"] = "false"
        }

        return properties
    }

    private fun modifyAxisAlignedBlocks(block: Any): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for (log in BlockTags.LOGS) {
            if (block == log) {
                properties["axis"] = getAxisDirection(
                    client.player.location.yaw,
                    client.player.location.pitch
                ).name.lowercase()
            }
        }

        if (block == Item.QUARTZ_PILLAR) {
            properties["axis"] = getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if (block == Item.CHAIN) {
            properties["axis"] = getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if (block == Item.BONE_BLOCK) {
            properties["axis"] = getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        if (block == Item.BASALT || block == Item.POLISHED_BASALT) {
            properties["axis"] = getAxisDirection(
                client.player.location.yaw,
                client.player.location.pitch
            ).name.lowercase()
        }

        return properties
    }

    private fun modifyBedBlocks(
        block: Item,
        cardinalDirection: String,
        event: BlockPlaceEvent
    ): MutableMap<String, String> {
        val properties = mutableMapOf<String, String>()

        for (bed in BlockTags.BEDS) {
            if (block == bed) {
                properties["facing"] = cardinalDirection
                properties["part"] = "foot"
                properties["occupied"] = "false"

                val headPos = when (cardinalDirection) {
                    "north" -> event.blockPos.copy(z = event.blockPos.z - 1)
                    "south" -> event.blockPos.copy(z = event.blockPos.z + 1)
                    "west" -> event.blockPos.copy(x = event.blockPos.x - 1)
                    "east" -> event.blockPos.copy(x = event.blockPos.x + 1)
                    else -> event.blockPos
                }

                val props: MutableMap<String, String> = mutableMapOf()
                props["facing"] = cardinalDirection
                props["part"] = "head"
                props["occupied"] = "false"

                val stateID = Item.getStateID(block, props)
                world.modifiedBlocks[headPos] = BlockWithMetadata(block.id, stateID)

                for (player in Bullet.players) {
                    player.sendPacket(ServerBlockChangePacket(headPos, stateID))
                }
            }
        }

        return properties
    }

    private fun sendEntities() {
        for (entity in world.entities) {
            client.sendPacket(
                ServerSpawnEntityPacket(
                    entity.first.entityID,
                    entity.second.uuid,
                    entity.second.entityType,
                    entity.second.location,
                    entity.second.velocityX,
                    entity.second.velocityY,
                    entity.second.velocityZ,
                )
            )
        }

        for (livingEntity in world.livingEntities) {
            client.sendPacket(
                ServerSpawnLivingEntityPacket(
                    livingEntity.first.entityID,
                    livingEntity.second.uuid,
                    livingEntity.second.entityType,
                    livingEntity.second.location,
                    livingEntity.second.headPitch,
                    livingEntity.second.velocityX,
                    livingEntity.second.velocityY,
                    livingEntity.second.velocityZ,
                )
            )
        }
    }

    private fun handleSpawnEgg(block: Item, event: BlockPlaceEvent) {
        val itemName = block.name.removeSuffix("_SPAWN_EGG")
        val entity = LivingEntities.entries.find { it.name.equals(itemName, true) }

        if (entity != null) {
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
        player.sendPacket(
            ServerParticlePacket(
                Particles.Block(block),
                false,
                blockPos.add(0.5, 0.5, 0.5),
                0.2f,
                0.22f,
                0.2f,
                0f,
                25
            )
        )
    }

    private fun getCardinalDirection(yaw: Float): String {
        val rot = (yaw % 360 + 360) % 360
        return when {
            rot >= 45 && rot < 135 -> "west"
            rot >= 135 && rot < 225 -> "north"
            rot >= 225 && rot < 315 -> "east"
            else -> "south"
        }
    }

    private fun getRotationalDirection(yaw: Float): Int {
        val normalizedYaw = (yaw % 360 + 360) % 360
        return ((normalizedYaw / 22.5).toInt() % 16)
    }

    private fun getAxisDirection(yaw: Float, pitch: Float): Axis {
        return when {
            pitch > 60f -> Axis.Y
            pitch < -60f -> Axis.Y
            abs(yaw) % 180 < 45 || abs(yaw) % 180 > 135 -> Axis.Z
            else -> Axis.X
        }
    }

    private fun handleBedClick(blockPos: BlockPositionType.BlockPosition) {
        val time = client.player.world!!.timeOfDay
        if (world.weather == 0) {
            if (time !in 12542..23459) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )
                return
            }
        } else {
            if (time !in 12010..23991) {
                client.player.sendMessage(
                    Component.text("You can only sleep at night")
                        .color(NamedTextColor.RED)
                )
                return
            }
        }

        val metadata = listOf(
            MetadataType.MetadataEntry(6.toByte(), 18, 2),
            MetadataType.MetadataEntry(13.toByte(), 10, true to blockPos),
        )

        for (player in players) {
            player.sendPacket(ServerEntityMetadataPacket(client.player.entityID, metadata))
        }

        client.player.world!!.sleepingPlayers += 1
        if (canSleepNow()) handleSleeping()
    }

    private fun handleWakeUp(player: Player) {
        if (!canSleepNow()) return

        val metadata = listOf(
            MetadataType.MetadataEntry(13.toByte(), 10, false to null),
            MetadataType.MetadataEntry(6.toByte(), 18, 0)
        )

        for (plr in players) {
            plr.sendPacket(ServerEntityMetadataPacket(player.entityID, metadata))
        }

        player.world!!.sleepingPlayers -= 1
    }

    private fun handleSleeping() {
        Bullet.scope.launch {
            delay(5.seconds)
            val world = client.player.world!!
            if (!canSleepNow()) return@launch

            for (player in players) {
                player.sendPacket(ServerChangeGameStatePacket(1, 0f))
                world.weather = 0
                player.setTimeOfDay(0)
                handleWakeUp(player)
            }
        }
    }

    private fun canSleepNow(): Boolean {
        val world = client.player.world!!
        val time = world.timeOfDay
        val totalPlayers = players.size
        val sleepingPlayers = world.sleepingPlayers

        return if (totalPlayers > 0 && sleepingPlayers >= totalPlayers / 2) {
            if (world.weather == 0) {
                time in 12542..23459
            } else {
                time in 12010..23991
            }
        } else {
            false
        }
    }

    private fun checkOrbs() {
        for (player in players) {
            val toRemove = mutableListOf<Entity>()
            for (orb in world.orbs) {
                val distance = sqrt(
                    (player.location.x - orb.location.x).pow(2) +
                            (player.location.y - orb.location.y).pow(2) +
                            (player.location.z - orb.location.z).pow(2)
                )

                if (distance <= 1.25) {
                    client.player.sendPacket(
                        ServerCollectItemPacket(
                            orb.entityID,
                            client.player.entityID,
                            1
                        )
                    )

                    client.player.sendPacket(ServerDestroyEntitiesPacket(intArrayOf(orb.entityID)))

                    client.player.sendPacket(
                        ServerSoundEffectPacket(
                            Sounds.ENTITY_EXPERIENCE_ORB_PICKUP,
                            SoundCategories.PLAYER,
                            client.player.location.x.toInt(),
                            client.player.location.y.toInt(),
                            client.player.location.z.toInt()
                        )
                    )

                    calculateXPLevels(client.player.totalXP + orb.xp)
                    toRemove.add(orb)
                }
            }

            world.orbs.removeAll(toRemove)
        }
    }

    fun xpToNextLevel(level: Int): Int = when {
        level < 16 -> 2 * level + 7
        level < 31 -> 5 * level - 38
        else -> 9 * level - 158
    }

    fun totalXPTillNextLevel(level: Int): Int = when {
        level <= 16 -> level * level + 6 * level
        level <= 31 -> (2.5 * level * level - 40.5 * level + 360).toInt()
        else -> (4.5 * level * level - 162.5 * level + 2220).toInt()
    }

    fun calculateXPLevels(totalXP: Int) {
        val player = client.player
        player.totalXP = totalXP

        var level = 0
        while (totalXPTillNextLevel(level + 1) <= player.totalXP) {
            level++
        }

        val xpIntoLevel = player.totalXP - totalXPTillNextLevel(level)
        val xpNeeded = xpToNextLevel(level).toFloat()

        player.level = level
        player.experienceBar = if (xpNeeded == 0f) 0f else xpIntoLevel / xpNeeded

        player.sendPacket(ServerSetExperiencePacket(player.experienceBar, player.level, player.totalXP))
    }

    fun handleOnlineModeJoin(packet: ClientLoginStartPacket) {
        if (Bullet.onlineMode) {
            val verifyToken = ByteArray(4).apply {
                SecureRandom().nextBytes(this)
            }

            client.verifyToken = verifyToken
            client.player.sendPacket(
                ServerEncryptionRequestPacket(
                    "",
                    Bullet.publicKey,
                    verifyToken
                )
            )
        }
    }

    private fun sendJoinGamePacket() {
        val player = client.player
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
    }

    private fun verifyPlayerToken(verifyToken: ByteArray) {
        if (!client.verifyToken.contentEquals(verifyToken)) {
            client.sendPacket(
                ServerLoginDisconnectPacket(
                    Component
                        .text("Invalid verification token", NamedTextColor.RED)
                )
            )

            client.close()
            return
        }
    }

    private fun dropItem(
        blockPos: BlockPositionType.BlockPosition,
        item: Int,
        vx: Short = 0, vy: Short = 0, vz: Short = 0
    ) {
        val drop = ItemStack.of(Item.getItemFromID(item) ?: Item.AIR)
        val itemEntity = DroppedItem()

        val loc = LocationType.Location(blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, 0f, 0f)
        itemEntity.location = loc

        world.items.add(Pair(itemEntity, drop))

        for (player in players) {
            player.sendPacket(
                ServerSpawnEntityPacket(
                    itemEntity.entityID, itemEntity.uuid,
                    37,
                    loc,
                    vx, vy, vz,
                    1
                )
            )

            player.sendPacket(
                ServerEntityMetadataPacket(
                    itemEntity.entityID,
                    listOf(MetadataType.MetadataEntry(7, 6, drop.toSlotData()))
                )
            )
        }
    }

    private fun checkItems() {
        val now = System.currentTimeMillis()
        val player = client.player
        val picked = mutableListOf<Pair<Entity, ItemStack>>()

        for (item in world.items) {
            if (now - item.first.spawnTimeMs < item.first.pickupDelayMs) continue

            val distance = sqrt(
                (player.location.x - item.first.location.x).pow(2) +
                        (player.location.y - item.first.location.y).pow(2) +
                        (player.location.z - item.first.location.z).pow(2)
            )

            if (distance <= 1.25) {
                player.sendPacket(
                    ServerCollectItemPacket(
                        item.first.entityID,
                        player.entityID,
                        1
                    )
                )

                player.sendPacket(ServerDestroyEntitiesPacket(intArrayOf(item.first.entityID)))

                player.sendPacket(
                    ServerSoundEffectPacket(
                        Sounds.ENTITY_ITEM_PICKUP,
                        SoundCategories.PLAYER,
                        player.location.x.toInt(),
                        player.location.y.toInt(),
                        player.location.z.toInt()
                    )
                )

                player.addItem(item.second)
                picked += item
            }
        }

        world.items.removeAll(picked)
    }

    private fun handleBlockDrop(blockPos: BlockPositionType.BlockPosition, status: Int) {
        val held = client.player.inventory.heldStack(client.player.selectedSlot)
        if (held.isAir) return

        val dropAll = status == 5
        val toDrop = if (dropAll) held else held.copy(count = 1)

        if (dropAll) {
            client.player.inventory.setHeldSlot(client.player.selectedSlot, null)
        } else {
            val newCount = held.count - 1
            client.player.inventory.setHeldSlot(
                client.player.selectedSlot,
                if (newCount > 0) held.copy(count = newCount) else null
            )
        }

        client.sendPacket(
            ServerSetSlotPacket(
                0, client.player.selectedSlot + 36,
                client.player.inventory.heldStack(client.player.selectedSlot).toSlotData()
            )
        )

        val yaw = Math.toRadians(client.player.location.yaw.toDouble())
        val pitch = Math.toRadians(client.player.location.pitch.toDouble())
        val forwardSpeed = 10
        val dx = -sin(yaw) * cos(pitch) * forwardSpeed
        val dy = -sin(pitch) * forwardSpeed + 0.2225
        val dz = cos(yaw) * cos(pitch) * forwardSpeed

        val vx = (dx * 8000).toInt().toShort()
        val vy = (dy * 8000).toInt().toShort()
        val vz = (dz * 8000).toInt().toShort()

        dropItem(client.player.location.toBlockPosition(), toDrop.id, vx, vy, vz)
    }

    private fun loginPlayer() {
        val player = client.player
        client.sendPacket(ServerLoginSuccessPacket(player.uuid, player.username))
        client.state = GameState.PLAY

        if (checkForBan()) return

        sendJoinGamePacket()
        client.sendPacket(ServerPlayerPositionAndLookPacket(player.location))

        players.add(player)
        readPlayerPersistentData()
        scheduleTimers()

        client.sendPacket(ServerChunkPacket(0, 0))
        sendSpawnPlayerPackets(player)

        client.sendPacket(ServerUpdateViewPositionPacket(player.chunkX, player.chunkZ))
        client.updatePlayerChunks(player.chunkX, player.chunkZ)

        sendBlockChanges()
        sendEntities()

        Bullet.storage.storage.writePlayerData(player)

        val joinEvent = PlayerJoinEvent(client.player)
        EventManager.fire(joinEvent)
        if (joinEvent.isCancelled) return

        val world = player.world!!
        player.setTimeOfDay(world.timeOfDay)
        if (world.weather == 1) player.sendPacket(ServerChangeGameStatePacket(2, 0f))
        else player.sendPacket(ServerChangeGameStatePacket(1, 0f))

        val (nodes, rootIndex) = buildCommandGraphFromDispatcher(CommandManager.dispatcher)
        client.sendPacket(ServerDeclareCommandsPacket(nodes, rootIndex))
    }

    private fun getToolMultiplier(heldItem: Item): Double =
        when(heldItem) {
            Item.WOODEN_PICKAXE, Item.WOODEN_AXE, Item.WOODEN_SHOVEL, Item.WOODEN_HOE -> 2.0
            Item.STONE_PICKAXE, Item.STONE_AXE, Item.STONE_SHOVEL, Item.STONE_HOE -> 4.0
            Item.IRON_PICKAXE, Item.IRON_AXE, Item.IRON_SHOVEL, Item.IRON_HOE -> 6.0
            Item.GOLDEN_PICKAXE, Item.GOLDEN_AXE, Item.GOLDEN_SHOVEL, Item.GOLDEN_HOE -> 12.0
            Item.DIAMOND_PICKAXE, Item.DIAMOND_AXE, Item.DIAMOND_SHOVEL, Item.DIAMOND_HOE -> 8.0
            Item.NETHERITE_PICKAXE, Item.NETHERITE_AXE, Item.NETHERITE_SHOVEL, Item.NETHERITE_HOE -> 9.0
            else -> 1.0
        }

    private fun canHarvestBlock(blockObj: Block, heldItem: Item): Boolean =
        canHarvestRock(blockObj, heldItem) && canHarvestMetal(blockObj, heldItem)

    @Suppress("ComplexCondition")
    private fun canHarvestRock(blockObj: Block, heldItem: Item): Boolean =
        BlockTags.ROCK_1.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_HAND.contains(heldItem) ||

        BlockTags.ROCK_2.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_WOODEN.contains(heldItem) ||

        BlockTags.ROCK_3.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_STONE.contains(heldItem) ||

        BlockTags.ROCK_4.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_IRON.contains(heldItem)

    @Suppress("ComplexCondition")
    private fun canHarvestMetal(blockObj: Block, heldItem: Item): Boolean =
        BlockTags.METAL_1.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_HAND.contains(heldItem) ||

        BlockTags.METAL_2.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_WOODEN.contains(heldItem) ||

        BlockTags.METAL_3.contains(blockObj) &&
        BlockTags.TOOLS.contains(heldItem) &&
        BlockTags.ABOVE_STONE.contains(heldItem)

    private fun getBestTool(blockObj: Block, heldItem: Item): Boolean =
        when {
            BlockTags.PICKAXE.contains(blockObj) && BlockTags.PICKAXES.contains(heldItem) -> true
            BlockTags.AXE.contains(blockObj) && BlockTags.AXES.contains(heldItem) -> true
            BlockTags.SHOVEL.contains(blockObj) && BlockTags.SHOVELS.contains(heldItem) -> true
            BlockTags.SWORD.contains(blockObj) && BlockTags.SWORDS.contains(heldItem) -> true
            else -> false
        }
}
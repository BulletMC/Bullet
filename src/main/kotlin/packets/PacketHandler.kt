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
            packet.apply(client)
        } catch(e: Exception) {
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
}
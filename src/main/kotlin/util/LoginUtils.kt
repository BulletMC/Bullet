package com.aznos.util

import com.aznos.Bullet
import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.commands.CommandManager
import com.aznos.commands.CommandManager.buildCommandGraphFromDispatcher
import com.aznos.datatypes.Slot
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.GameMode
import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import com.aznos.packets.login.`in`.ClientLoginStartPacket
import com.aznos.packets.login.out.ServerEncryptionRequestPacket
import com.aznos.packets.login.out.ServerLoginSuccessPacket
import com.aznos.packets.play.out.ServerBlockChangePacket
import com.aznos.packets.play.out.ServerBlockEntityDataPacket
import com.aznos.packets.play.out.ServerChangeGameStatePacket
import com.aznos.packets.play.out.ServerChunkPacket
import com.aznos.packets.play.out.ServerDeclareCommandsPacket
import com.aznos.packets.play.out.ServerJoinGamePacket
import com.aznos.packets.play.out.ServerPlayerPositionAndLookPacket
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.aznos.packets.play.out.ServerSpawnPlayerPacket
import com.aznos.packets.play.out.ServerUpdateHealthPacket
import com.aznos.packets.play.out.ServerUpdateViewPositionPacket
import com.aznos.packets.play.out.ServerWindowItemsPacket
import com.aznos.world.items.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.querz.nbt.tag.CompoundTag
import java.security.SecureRandom
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object LoginUtils {
    /**
     * Handles the login process for a player.
     *
     * @param client The client session of the player logging in.
     */
    fun loginPlayer(client: ClientSession) {
        val player = client.player
        client.sendPacket(ServerLoginSuccessPacket(player.uuid, player.username))
        client.state = GameState.PLAY

        if(checkForBan(client)) return

        sendJoinGamePacket(client)
        client.sendPacket(ServerPlayerPositionAndLookPacket(player.location))

        players.add(player)
        readPlayerPersistentData(client)
        scheduleTimers(client)

        client.sendPacket(ServerChunkPacket(0, 0))
        sendSpawnPlayerPackets(client)

        client.sendPacket(ServerUpdateViewPositionPacket(player.chunkX, player.chunkZ))
        client.updatePlayerChunks(player.chunkX, player.chunkZ)

        sendBlockChanges(client)
        sendEntities(client)

        Bullet.storage.storage.writePlayerData(player)

        val joinEvent = PlayerJoinEvent(client.player)
        EventManager.fire(joinEvent)
        if (joinEvent.isCancelled) return

        val world = player.world!!
        player.setTimeOfDay(world.timeOfDay)
        if(world.weather == 1) player.sendPacket(ServerChangeGameStatePacket(2, 0f))
        else player.sendPacket(ServerChangeGameStatePacket(1, 0f))

        val (nodes, rootIndex) = buildCommandGraphFromDispatcher(CommandManager.dispatcher)
        client.sendPacket(ServerDeclareCommandsPacket(nodes, rootIndex))
    }

    /**
     * Handles when the server is in online mode and a player attempts to join.
     *
     * @param client The client session of the player connecting.
     */
    fun handleOnlineModeJoin(client: ClientSession) {
        if(Bullet.onlineMode) {
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

    /**
     * Checks if the player is banned and handles the ban logic
     *
     * @param client The client session of the player.
     * @return True if the player is banned, false otherwise.
     */
    private fun checkForBan(client: ClientSession): Boolean {
        val ban = Bullet.storage.getPlayerBan(client.player.uuid) ?: return false

        val durationMillis = ban.duration.inWholeMilliseconds
        val banEnd = ban.currentTime + durationMillis
        val now = System.currentTimeMillis()

        if(durationMillis > 0 && now >= banEnd) {
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

    private fun sendJoinGamePacket(client: ClientSession) {
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

    private fun readPlayerPersistentData(client: ClientSession) {
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
        client.player.sendHeldItemUpdate(client)
        OrbUtils.calculateXPLevels(player.totalXP, client)

        player.sendPacket(
            ServerUpdateHealthPacket(
                player.status.health.toFloat(),
                player.status.foodLevel,
                player.status.saturation
            )
        )

        player.sendPacket(ServerPlayerPositionAndLookPacket(player.location))
    }

    private fun scheduleTimers(client: ClientSession) {
        client.sendPlayerSpawnPacket()
        client.scheduleKeepAlive()
        client.scheduleHalfSecondUpdate()

        if(Bullet.shouldPersist) client.scheduleSaving()
    }

    private fun sendSpawnPlayerPackets(client: ClientSession) {
        val player = client.player
        for(otherPlayer in players) {
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

        for(existingPlayer in players) {
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

    private fun sendBlockChanges(client: ClientSession) {
        val world = client.player.world ?: return
        if(world.modifiedBlocks.isEmpty()) return
        for((pos, meta) in world.modifiedBlocks) {
            client.player.sendPacket(
                ServerBlockChangePacket(
                    pos, meta.stateID
                )
            )

            if(meta.textLines != null) {
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

    private fun sendEntities(client: ClientSession) {
        val world = client.player.world ?: return
        for(entity in world.entities) {
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

        for(livingEntity in world.livingEntities) {
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
}
package com.aznos.world

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.entity.DroppedItem
import com.aznos.entity.Entity
import com.aznos.entity.OrbEntity
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.livingentity.NPCEntity
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PlayerProperty
import com.aznos.packets.play.out.ServerDestroyEntitiesPacket
import com.aznos.packets.play.out.ServerPlayerInfoPacket
import com.aznos.packets.play.out.ServerSoundEffectPacket
import com.aznos.packets.play.out.ServerSpawnPlayerPacket
import com.aznos.packets.play.out.movement.ServerEntityHeadLook
import com.aznos.packets.play.out.movement.ServerEntityPositionAndRotationPacket
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.util.schedule
import com.aznos.world.blocks.Block
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.Difficulty
import com.aznos.world.data.EntityData
import com.aznos.world.data.TimeOfDay
import com.aznos.world.items.ItemStack
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds
import com.github.f4b6a3.uuid.UuidCreator
import com.github.f4b6a3.uuid.enums.UuidLocalDomain
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import java.net.Socket
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a world in the game
 *
 * @param name The name of the world, default is just "world"
 *
 * @property weather The weather of the world, 0 for clear, 1 for rain
 * @property worldAge How old the world is
 * @property timeOfDay The time of day in the server, 0-24000
 * @property difficulty The difficulty of the world
 * @property modifiedBlocks A map of all the blocks that have been modified in the world
 * beside the default grass chunks that spawn in
 */
@Suppress("TooManyFunctions")
class World(
    val name: String,
    private val storage: AbstractWorldStorage
) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var sleepingPlayers: Int = 0
    var difficulty: Difficulty = Difficulty.NORMAL

    val livingEntities = mutableListOf<Pair<LivingEntity, EntityData>>()
    val entities = mutableListOf<Pair<Entity, EntityData>>()
    val npcs = mutableListOf<NPCEntity>()
    val orbs = mutableListOf<OrbEntity>()
    val items = mutableListOf<Pair<DroppedItem, ItemStack>>()

    lateinit var modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>
    private val lastLocations = mutableMapOf<Int, LocationType.Location>()

    init {
        loadWorldsData()
        cleanItems()

        Bullet.scope.schedule(50.milliseconds) {
            livingEntities.forEach { it.first.tickAI(this@World) }
        }
    }

    private fun loadWorldsData() {
        livingEntities.clear()
        entities.clear()
        this.modifiedBlocks = storage.readBlockData()

        val data = storage.readWorldData() ?: return

        val difficulty = Difficulty.getDifficultyFromID(data.difficulty)

        this.difficulty = difficulty
        this.weather = if (data.raining) 1 else 0
        this.timeOfDay = data.timeOfDay

        if(storage.readEntities() != null) {
            for(entity in storage.readEntities()!!) {
                when(entity.isLiving) {
                    true -> {
                        val living = LivingEntity()
                        livingEntities.add(
                            Pair(
                                living,
                                entity
                            )
                        )

                        living.navigator.moveTo(entity.location.toVec3D(), this)
                    }
                    false -> {
                        entities.add(
                            Pair(
                                Entity(),
                                entity
                            )
                        )
                    }
                }
            }
        }
    }

    fun save() {
        storage.writeWorldData(this)
        storage.writeBlockData(modifiedBlocks)

        val living = livingEntities.map { it.second }
        val nonLiving = entities.map { it.second }
        storage.writeEntities(living + nonLiving)
    }

    /**
     * Plays a sound at a given location in the world to all players
     *
     * @param sound The sound to play
     * @param category The sound category that the sound falls under
     * @param position The block position at where the sound should play
     * @param volume Capped between 0.0f and 1.0f
     * @param pitch Float between 0.5 and 2.0
     */
    fun playSound(
        sound: Sounds,
        category: SoundCategories,
        position: BlockPositionType.BlockPosition,
        volume: Float = 1.0f,
        pitch: Float = 1.0f
    ) {
        for(player in Bullet.players) {
            player.sendPacket(ServerSoundEffectPacket(
                sound, category,
                position.x.toInt(), position.y.toInt(), position.z.toInt(),
                volume, pitch
            ))
        }
    }

    fun spawnNPC(
        name: String, location: LocationType.Location,
        skinProps: List<PlayerProperty>,
        appearInTab: Boolean = false
    ): NPCEntity {
        val npc = NPCEntity().apply {
            this.username = name
            this.uuid = UuidCreator.getDceSecurity(UuidLocalDomain.LOCAL_DOMAIN_PERSON, entityID)
            this.location = location
            this.properties += skinProps
        }

        npcs += npc
        broadcastSpawn(npc, appearInTab)
        return npc
    }

    /**
     * Periodically cleans up items for lag purposes, items will be removed if they are below y = -128
     * or have been dropped for more than 5 minutes
     */
    private fun cleanItems() {
        Bullet.scope.launch {
            while(isActive) {
                delay(10.seconds)
                val now = System.currentTimeMillis()
                val toRemove = mutableListOf<Pair<DroppedItem, ItemStack>>()
                for(item in items) {
                    if(item.first.location.y < -128 || now - item.first.spawnTimeMs > 5 * 60 * 1000) {
                        for(player in Bullet.players) {
                            player.sendPacket(ServerDestroyEntitiesPacket(intArrayOf(item.first.entityID)))
                        }

                        toRemove += item
                    }
                }

                items.removeAll(toRemove)
            }
        }
    }

    private fun broadcastSpawn(npc: NPCEntity, appearInTab: Boolean = false) {
        val addEntry = ServerPlayerInfoPacket(
            0, npc.uuid, npc.username, npc.properties
        )

        val removeEntry = ServerPlayerInfoPacket(
            4, npc.uuid
        )

        val spawn = ServerSpawnPlayerPacket(npc.entityID, npc.uuid, npc.location)
        for(player in Bullet.players) {
            player.sendPacket(addEntry)
            player.sendPacket(spawn)

            if(!appearInTab) {
                Bullet.scope.launch {
                    delay(50.milliseconds)
                    player.sendPacket(removeEntry)
                }
            }
        }
    }

    /**
     * Broadcasts an entity update to all players in the world
     *
     * @param entity The entity to broadcast
     */
    fun broadcastEntityUpdate(entity: Entity) {
        val newLoc = entity.location
        val prevLoc = lastLocations.getOrPut(entity.entityID) { newLoc }

        fun d(a: Double, b: Double) =
            ((a - b) * 4096).toInt().coerceIn(-32768, 32767).toShort()

        val dx = d(newLoc.x, prevLoc.x)
        val dy = d(newLoc.y, prevLoc.y)
        val dz = d(newLoc.z, prevLoc.z)

        val noPositionChange = dx == (0).toShort() && dy == (0).toShort() && dz == (0).toShort()
        val noRotationChange = newLoc.yaw == prevLoc.yaw && newLoc.pitch == prevLoc.pitch
        if(noPositionChange && noRotationChange) return

        for(player in Bullet.players) {
            if(player.entityID == entity.entityID) continue
            player.sendPacket(ServerEntityPositionAndRotationPacket(
                entity.entityID,
                dx, dy, dz,
                newLoc.yaw, newLoc.pitch,
                true
            ))

            player.sendPacket(ServerEntityHeadLook(entity.entityID, newLoc.yaw))
        }

        lastLocations[entity.entityID] = newLoc
    }

    fun getHighestSolidBlockY(x: Double, z: Double, maxY: Int = 255): Int {
        for(y in maxY downTo 0) {
            val pos = BlockPositionType.BlockPosition(x, y.toDouble(), z)
            val id = modifiedBlocks[pos]?.blockID ?: if(y == 0) Block.GRASS_BLOCK.id else Block.AIR.id

            if(id != Block.AIR.id) return y
        }

        return -1
    }
}
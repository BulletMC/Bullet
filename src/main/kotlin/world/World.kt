package com.aznos.world

import com.aznos.Bullet
import com.aznos.datatypes.BlockPositionType
import com.aznos.entity.Entity
import com.aznos.entity.OrbEntity
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.packets.play.out.ServerSoundEffectPacket
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.Difficulty
import com.aznos.world.data.EntityData
import com.aznos.world.data.TimeOfDay
import com.aznos.world.items.ItemStack
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds

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
    val orbs = mutableListOf<OrbEntity>()
    val items = mutableListOf<Pair<Entity, ItemStack>>()

    lateinit var modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>

    init {
        loadWorldsData()
    }

    private fun loadWorldsData() {
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
                        livingEntities.add(
                            Pair(
                                LivingEntity(),
                                entity
                            )
                        )
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

        for(entity in entities) {
            storage.writeEntity(entity.second)
        }

        for(livingEntity in livingEntities) {
            storage.writeEntity(livingEntity.second)
        }
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
}
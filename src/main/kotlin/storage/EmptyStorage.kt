package com.aznos.storage

import com.aznos.datatypes.BlockPositionType
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.BanData
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.World
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.PlayerData
import com.aznos.world.data.WorldData
import java.util.*

/**
 * Represent an empty storage that do not store modifications
 */
class EmptyStorage : AbstractServerStorage {
    override fun prepareWorldStorage(name: String): AbstractWorldStorage {
        return EmptyWorldStorage(name)
    }

    override fun readPlayerData(uuid: UUID): PlayerData? {
        return null
    }

    override fun writePlayerData(data: PlayerData): Boolean {
        return true
    }

    override fun writePlayerData(player: Player): Boolean {
        return true
    }

    override fun readBannedList(): Collection<BanData> {
        return Collections.emptyList()
    }

    override fun writeBannedList(banned: Collection<BanData>): Boolean {
        return true
    }

    class EmptyWorldStorage(private val name: String) : AbstractWorldStorage {
        override fun getName(): String {
            return name
        }

        override fun readWorldData(): WorldData? {
            return null
        }

        override fun writeWorldData(data: WorldData): Boolean {
            return true
        }

        override fun writeWorldData(world: World): Boolean {
            return true
        }

        override fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> {
            return mutableMapOf()
        }

        override fun writeBlockData(
            modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>
        ): Boolean {
            return true
        }

        override fun writeEntity(entityData: EntityData): Boolean {
            return true
        }

        override fun readEntities(): List<EntityData>? {
            return null
        }
    }
}
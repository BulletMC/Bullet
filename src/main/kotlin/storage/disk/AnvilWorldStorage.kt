package com.aznos.storage.disk

import com.aznos.datatypes.BlockPositionType
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.World
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.WorldData
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.File

class AnvilWorldStorage(private val name: String, val root: File) : AbstractWorldStorage {
    private val nbt = Nbt()
    private val levelDat = File(root, "level.dat")
    private var cachedData: WorldData? = null

    override fun getName() = name
    override fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> = mutableMapOf()
    override fun writeBlockData(modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>) = true

    override fun readWorldData(): WorldData? {
        //fill in
        return null
    }

    override fun writeWorldData(data: WorldData): Boolean {
        //f.ill in
        return true
    }

    override fun writeWorldData(world: World): Boolean {
        return writeWorldData(WorldData(world.difficulty.id, world.weather == 1, world.timeOfDay))
    }

    //TODO: Implement entity reading and writing
    override fun writeEntities(entities: List<EntityData>) = true
    override fun readEntities(): List<EntityData>? = null
}
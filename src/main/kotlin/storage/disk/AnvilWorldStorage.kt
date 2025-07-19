package com.aznos.storage.disk

import com.aznos.datatypes.BlockPositionType
import com.aznos.storage.world.AbstractWorldStorage
import com.aznos.world.World
import com.aznos.world.data.BlockWithMetadata
import com.aznos.world.data.EntityData
import com.aznos.world.data.WorldData
import net.querz.nbt.io.NBTUtil
import net.querz.nbt.tag.CompoundTag
import java.io.File
import java.io.IOException
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

class AnvilWorldStorage(private val name: String, val root: File) : AbstractWorldStorage {
    private val levelDat = File(root, "level.dat")
    private val lock = ReentrantReadWriteLock()

    override fun getName() = name
    override fun readBlockData(): MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata> = mutableMapOf()
    override fun writeBlockData(modifiedBlocks: MutableMap<BlockPositionType.BlockPosition, BlockWithMetadata>) = true

    private fun readLevelRoot(): CompoundTag? {
        if(!levelDat.exists()) return null
        return try {
            val named = NBTUtil.read(levelDat)
            named.tag as? CompoundTag
        } catch(e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun readWorldData(): WorldData? {
        val root = readLevelRoot() ?: return null
        val data = root.getCompoundTag("Data")
        val difficulty = data.getByte("Difficulty").toInt()
        val dayTime = data.getLong("DayTime")
        val raining = (data.getByte("raining").toInt() == 1) || (data.getInt("rainTime") > 0 && data.getByte("raining").toInt() != 0)

        return WorldData(difficulty, raining, (dayTime % 24000 + 24000) % 24000)
    }

    override fun writeWorldData(data: WorldData): Boolean = lock.write {
        if(!levelDat.exists()) return false
        val root = readLevelRoot() ?: return false
        val levelData = root.getCompoundTag("Data")

        levelData.putLong("DayTime", data.timeOfDay)
        val prevTotal = if(levelData.containsKey("Time")) levelData.getLong("Time") else data.timeOfDay
        val newTotal = if(data.timeOfDay > prevTotal) data.timeOfDay else prevTotal + 20
        levelData.putLong("Time", newTotal)

        val raining = data.raining
        levelData.putByte("raining", (if(raining) 1 else 0).toByte())
        levelData.putInt("rainTime", if(raining) 6000 else 0)
        levelData.putByte("thundering", 0.toByte())
        levelData.putInt("thunderTime", 0)

        levelData.putByte("Difficulty", data.difficulty.toByte())
        NBTUtil.write(root, levelDat)
        true
    }

    override fun writeWorldData(world: World): Boolean {
        return writeWorldData(WorldData(world.difficulty.id, world.weather == 1, world.timeOfDay))
    }

    //TODO: Implement entity reading and writing
    override fun writeEntities(entities: List<EntityData>) = true
    override fun readEntities(): List<EntityData>? = null
}
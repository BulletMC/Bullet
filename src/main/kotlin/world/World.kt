package com.aznos.world

import com.aznos.world.data.Difficulty
import com.aznos.world.data.TimeOfDay
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.io.CompressionType
import dev.dewy.nbt.tags.collection.CompoundTag
import dev.dewy.nbt.tags.collection.ListTag
import dev.dewy.nbt.tags.primitive.ByteTag
import dev.dewy.nbt.tags.primitive.IntTag
import dev.dewy.nbt.tags.primitive.StringTag
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class World(val name: String) {
    var weather = 0
    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time
    var difficulty: Difficulty = Difficulty.NORMAL

    /**
     * Saves the current world state
     *
     * @return Whether the operation was successful or not
     */
    fun saveWorld(): Boolean {
        createDirectoryIfNotExists(Paths.get("./$name"))
        createDirectoryIfNotExists(Paths.get("./$name/data"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM-1"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM-1/region"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM1"))
        createDirectoryIfNotExists(Paths.get("./$name/DIM1/region"))
        createDirectoryIfNotExists(Paths.get("./$name/players"))
        createDirectoryIfNotExists(Paths.get("./$name/region"))
        createFileIfNotExists(Paths.get("./$name/level.dat"))

        writeLevelDat()

        return true
    }

    /**
     * Creates a directory if it does not already exist
     *
     * @param path The path of the directory to create
     */
    private fun createDirectoryIfNotExists(path: Path) {
        if(!Files.exists(path)) {
            Files.createDirectory(path)
        }
    }

    /**
     * Creates a file if it does not already exist
     *
     * @param path The path of the file to create
     */
    private fun createFileIfNotExists(path: Path) {
        if(!Files.exists(path)) {
            Files.createFile(path)
        }
    }

    private fun writeLevelDat() {
        val root = CompoundTag("")
        val data = CompoundTag("Data")

        data.putInt("WanderingTraderSpawnChance", 25)
        data.putDouble("BorderCenterZ", 0.0)
        data.putByte("Difficulty", 1)
        data.putLong("BorderSizeLerpTime", 0L)
        data.putByte("raining", 0)
        data.putLong("Time", 1637L)
        data.putInt("GameType", 0)

        data.putString("ServerBrands", "vanilla")

        data.putDouble("BorderCenterX", 0.0)
        data.putDouble("BorderDamagePerBlock", 0.2)
        data.putDouble("BorderWarningBlocks", 5.0)

        val worldGenSettings = CompoundTag("WorldGenSettings")
        worldGenSettings.putByte("bonus_chest", 0)
        worldGenSettings.putLong("seed", -4754100011618039152L)
        worldGenSettings.putByte("generate_features", 1)

        val dimensions = CompoundTag("dimensions")

        fun createDimension(id: String, settings: String, biomeSourceType: String, preset: String? = null): CompoundTag {
            val biomeSource = CompoundTag("biome_source")
            biomeSource.putLong("seed", -4754100011618039152L)
            if(preset != null) biomeSource.putString("preset", preset)
            else biomeSource.put("large_biomes", ByteTag("large_biomes", 0))
            biomeSource.putString("type", biomeSourceType)

            val generator = CompoundTag("generator")
            generator.putString("settings", settings)
            generator.putLong("seed", -4754100011618039152L)
            generator.put<CompoundTag>("biome_source", biomeSource)
            generator.putString("type", "minecraft:noise")

            val dim = CompoundTag(id)
            dim.put<CompoundTag>("generator", generator)
            dim.putString("type", id)
            return dim
        }

        dimensions.put<CompoundTag>(createDimension("minecraft:overworld", "minecraft:overworld", "minecraft:vanilla_layered"))
        dimensions.put<CompoundTag>(createDimension("minecraft:the_nether", "minecraft:nether", "minecraft:multi_noise", "minecraft:nether"))
        dimensions.put<CompoundTag>(createDimension("minecraft:the_end", "minecraft:end", "minecraft:the_end"))

        worldGenSettings.put<CompoundTag>("dimensions", dimensions)
        data.put<CompoundTag>(worldGenSettings)

        val dragonFight = CompoundTag("DragonFight")
        val gateways = ListTag<IntTag>("Gateways")
        listOf(13, 16, 5, 6, 2, 9, 4, 11, 18, 8, 12, 15, 14, 3, 7, 1, 19, 17, 10, 0).forEach {
            gateways.add(IntTag(it))
        }

        dragonFight.put<ListTag<IntTag>>(gateways)
        dragonFight.putByte("DragonKilled", 1)
        dragonFight.putByte("PreviouslyKilled", 1)
        data.put<CompoundTag>(dragonFight)

        data.putDouble("BorderSizeLerpTarget", 60000000.0)
        root.put<CompoundTag>(data)

        val nbt = Nbt()
        val levelDatPath = Paths.get("./$name/level.dat")
        nbt.toFile(root, levelDatPath.toFile(), CompressionType.GZIP)
    }
}
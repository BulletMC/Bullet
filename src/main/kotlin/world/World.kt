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

        val version = CompoundTag("Version")
        version.putByte("SnapShot", 0)
        version.putInt("Id", 2586)
        version.putString("Name", "1.16.5")
        data.put<CompoundTag>(version)

        data.putLong("DayTime", 1637L)
        data.putByte("initialized", 1)
        data.putByte("WasModded", 0)
        data.putByte("allowCommands", 0)
        data.putInt("WanderingTraderSpawnDelay", 22800)
        data.putByte("hardcore", 0)
        data.putByte("DifficultyLocked", 0)
        data.putInt("SpawnY", 81)
        data.putInt("rainTime", 50831)
        data.putInt("thunderTime", 165012)
        data.putInt("SpawnZ", -96)
        data.putInt("SpawnX", -240)
        data.putInt("clearWeatherTime", 0)
        data.putByte("thundering", 0)
        data.putFloat("SpawnAngle", 0.0f)
        data.putInt("version", 19133)
        data.putDouble("BorderSafeZone", 5.0)
        data.putLong("LastPlayed", 1743016248982L)
        data.putDouble("BorderWarningTime", 15.0)
        data.putList("ScheduledEvents", emptyList<CompoundTag>())
        data.putString("LevelName", "world")
        data.putDouble("BorderSize", 60000000.0)
        data.putInt("DataVersion", 2586)

        data.put<CompoundTag>("CustomBossEvents", CompoundTag("CustomBossEvents"))
        val gameRules = CompoundTag("GameRules")
        val rules = mapOf(
            "doFireTick" to "true",
            "maxCommandChainLength" to "65536",
            "fireDamage" to "true",
            "reducedDebugInfo" to "false",
            "disableElytraMovementCheck" to "false",
            "announceAdvancements" to "true",
            "drowningDamage" to "true",
            "commandBlockOutput" to "true",
            "forgiveDeadPlayers" to "true",
            "doMobSpawning" to "true",
            "maxEntityCramming" to "24",
            "disableRaids" to "false",
            "doWeatherCycle" to "true",
            "doDaylightCycle" to "true",
            "showDeathMessages" to "true",
            "doTileDrops" to "true",
            "universalAnger" to "false",
            "doInsomnia" to "true",
            "doImmediateRespawn" to "false",
            "naturalRegeneration" to "true",
            "doMobLoot" to "true",
            "fallDamage" to "true",
            "keepInventory" to "false",
            "doEntityDrops" to "true",
            "doLimitedCrafting" to "false",
            "mobGriefing" to "true",
            "randomTickSpeed" to "3",
            "spawnRadius" to "10",
            "doTraderSpawning" to "true",
            "logAdminCommands" to "true",
            "spectatorsGenerateChunks" to "true",
            "sendCommandFeedback" to "true",
            "doPatrolSpawning" to "true"
        )

        for((key, value) in rules) {
            gameRules.putString(key, value)
        }

        data.put<CompoundTag>("GameRules", gameRules)

        val dataPacks = CompoundTag("DataPacks")
        val enabled = ListTag<StringTag>("Enabled")
        enabled.add(StringTag("vanilla"))
        dataPacks.put<CompoundTag>("Enabled", enabled)

        val disabled = ListTag<StringTag>("Disabled")
        dataPacks.put<CompoundTag>("Disabled", disabled)

        data.put<CompoundTag>("DataPacks", dataPacks)
        root.put<CompoundTag>(data)

        val nbt = Nbt()
        val levelDatPath = Paths.get("./$name/level.dat")
        nbt.toFile(root, levelDatPath.toFile(), CompressionType.GZIP)
    }
}
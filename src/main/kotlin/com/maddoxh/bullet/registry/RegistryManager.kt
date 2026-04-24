package com.maddoxh.bullet.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.maddoxh.bullet.Bullet.Companion.logger
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import kotlin.math.floor

object RegistryManager {
    val REGISTRY_SEND_ORDER = listOf(
        "minecraft:damage_type",
        "minecraft:dimension_type",
        "minecraft:worldgen/biome",
        "minecraft:painting_variant",
        "minecraft:wolf_variant",
        "minecraft:wolf_sound_variant",
        "minecraft:cat_variant",
        "minecraft:chicken_variant",
        "minecraft:cow_variant",
        "minecraft:frog_variant",
        "minecraft:pig_variant",
        "minecraft:chat_type",
        "minecraft:banner_pattern",
        "minecraft:enchantment",
        "minecraft:instrument",
        "minecraft:jukebox_song",
        "minecraft:trim_material",
        "minecraft:trim_pattern",
        "minecraft:dialog"
    )

    private val registries = mutableMapOf<String, Map<String, CompoundBinaryTag>>()

    fun load() {
        val stream = RegistryManager::class.java.classLoader
            .getResourceAsStream("registry_data.json")
            ?: error("registry_data.json file not found")

        val root = JsonParser.parseReader(stream.reader()) as JsonObject
        for((registryID, registryElement) in root.entrySet()) {
            val registryObj = registryElement.asJsonObject
            val entries = mutableMapOf<String, CompoundBinaryTag>()

            for((entryID, entryElement) in registryObj.entrySet()) {
                val entryObj = entryElement.asJsonObject
                entries[entryID] = jsonToNBT(entryObj)
            }

            registries[registryID] = entries
        }

        logger.info("[Registry] Loaded ${registries.size} registries")
    }

    fun getRegistry(id: String): Map<String, CompoundBinaryTag> =
        registries[id] ?: emptyMap()

    private fun jsonToNBT(obj: JsonObject): CompoundBinaryTag {
        val builder = CompoundBinaryTag.builder()

        for((key, element) in obj.entrySet()) {
            when {
                element.isJsonObject    -> builder.put(key, jsonToNBT(element.asJsonObject))
                element.isJsonArray     -> builder.put(key, jsonToNBTList(element.asJsonArray, key))
                element.isJsonPrimitive -> {
                    val prim = element.asJsonPrimitive
                    when {
                        prim.isBoolean -> builder.put(key, ByteBinaryTag.byteBinaryTag(if(prim.asBoolean) 1 else 0))
                        prim.isNumber  -> {
                            val n = prim.asNumber
                            when {
                                n.toDouble() == floor(n.toDouble()) && !n.toString().contains('.') -> {
                                    val long = n.toLong()
                                    if(long in Int.MIN_VALUE..Int.MAX_VALUE) {
                                        builder.put(key, IntBinaryTag.intBinaryTag(long.toInt()))
                                    } else {
                                        builder.put(key, LongBinaryTag.longBinaryTag(long))
                                    }
                                }

                                else -> builder.put(key, DoubleBinaryTag.doubleBinaryTag(n.toDouble()))
                            }
                        }

                        prim.isString -> builder.put(key, StringBinaryTag.stringBinaryTag(prim.asString))
                    }
                }
            }
        }

        return builder.build()
    }

    private fun jsonToNBTList(arr: JsonArray, key: String): ListBinaryTag {
        if(arr.isEmpty) return ListBinaryTag.empty()

        val first = arr[0]
        return when {
            first.isJsonObject -> ListBinaryTag.listBinaryTag(
                BinaryTagTypes.COMPOUND,
                arr.map { jsonToNBT(it.asJsonObject) }
            )

            first.isJsonPrimitive && first.asJsonPrimitive.isString -> ListBinaryTag.listBinaryTag(
                BinaryTagTypes.STRING,
                arr.map { StringBinaryTag.stringBinaryTag(it.asString) }
            )

            first.isJsonPrimitive && first.asJsonPrimitive.isNumber -> ListBinaryTag.listBinaryTag(
                BinaryTagTypes.INT,
                arr.map { IntBinaryTag.intBinaryTag(it.asInt) }
            )

            else -> ListBinaryTag.empty()
        }
    }
}
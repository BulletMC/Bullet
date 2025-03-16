package com.aznos.util

import com.aznos.Main
import com.google.gson.JsonObject

object DataFetcher {

    private val dataKeys: JsonObject = Main.gson.fromJson(Main.dataFolder.resolve("data/dataPaths.json").readText(), JsonObject::class.java).getAsJsonObject("pc").getAsJsonObject(Main.VERSION)
    private val cache = mutableMapOf<String, JsonObject>()

    fun getData(key: String): JsonObject {
        if (cache.containsKey(key)) return cache[key]!!
        if (!dataKeys.has(key)) throw IllegalArgumentException("Unknown key \"$key\"")

        val json = Main.gson.fromJson(Main.dataFolder.resolve("data/").resolve(dataKeys.get(key).asString).readText(), JsonObject::class.java)
        cache[key] = json
        return json
    }

    fun getCommon(key: String): JsonObject {
        if (cache.containsKey("common_$key")) return cache["common_$key"]!!
        val json = Main.gson.fromJson(Main.dataFolder.resolve("data/pc/common/$key.json").readText(), JsonObject::class.java)
        cache["common_$key"] = json
        return json
    }

}
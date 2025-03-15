package com.aznos.world

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Represents a minecraft block with an ID and minecraft name
 *
 * @param id The ID of the block
 * @param minecraftID The minecraft id, (e.g. "minecraft:stone")
 */
data class Block(val id: Int, val minecraftID: String) {
    @Suppress("unused")
    companion object {
        private val blocksByID = mutableMapOf<Int, Block>()
        private val blocksByName = mutableMapOf<String, Block>()

        /**
         * Loads all the blocks from the provided dimension codec JSON file
         *
         * @param inputStream The input stream of the JSON file
         */
        fun loadBlocksFromJSON(inputStream: InputStream) {
            val reader = InputStreamReader(inputStream)
            val json = JsonParser.parseReader(reader).asJsonObject

            val blockRegistry = json.getAsJsonObject("minecraft:block")?.getAsJsonArray("value")
            require(blockRegistry is JsonArray) { "Invalid format: Expected an array for 'minecraft:block.value'" }

            var idCounter = 0
            for(blockElement in blockRegistry) {
                if(blockElement.isJsonObject) {
                    val blockObj = blockElement.asJsonObject
                    val blockName = blockObj.get("name")?.asString

                    if(blockName != null) {
                        val block = Block(idCounter, blockName)
                        blocksByID[idCounter] = block
                        blocksByName[blockName] = block
                        idCounter++
                    }
                }
            }
        }


        /**
         * Retrieves a block by its numerical ID
         *
         * @param id The numerical ID of the block
         * @return The block object if found, null otherwise
         */
        fun getBlockByID(id: Int): Block? {
            return blocksByID[id]
        }

        /**
         * Retrieves a block by its minecraft name
         *
         * @param name The minecraft name of the block (e.g. "minecraft:stone")
         * @return The block object if found, null otherwise
         */
        fun getBlockByName(name: String): Block? {
            return blocksByName[name]
        }
    }
}
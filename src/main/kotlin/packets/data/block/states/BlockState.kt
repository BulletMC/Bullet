package com.aznos.packets.data.block.states

import com.aznos.Bullet
import com.aznos.packets.data.block.states.type.StateType
import com.aznos.packets.data.block.states.type.StateTypes.byName
import com.aznos.packets.data.block.states.type.StateValue
import kotlinx.serialization.json.*
import java.io.File

class BlockState {

    val type: StateType
    private val data: MutableMap<StateValue, Any> = HashMap(0)
    val globalId: Int

    constructor(type: StateType, data: List<String>?, globalId: Int) {
        this.type = type
        if (data != null) {
            for (s in data) {
                try {
                    val split = s.split("=")
                    val value = StateValue.byName(split[0])!!
                    this.data[value] = value.parser(split[1].uppercase())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        this.globalId = globalId
    }

    constructor(type: StateType, data: Map<StateValue, Any>?, globalId: Int) {
        this.type = type
        data?.let {
            this.data += it
        }
        this.globalId = globalId
    }

    companion object {

        val BY_STRING = HashMap<String, BlockState>()
        val BY_ID = HashMap<Int, BlockState>()
        val INTO_STRING = HashMap<BlockState, String>()
        val INTO_ID = HashMap<BlockState, Int>()
        val DEFAULT_STATES = HashMap<StateType, BlockState>()

        private fun loadMappings() {
            val file = File("mappings/block/block_mappings.json")
            val json = Json.parseToJsonElement(file.readText())

            var id = 0

            for ((key, element) in json.jsonObject) { // Array of states
                val stateObject = element.jsonObject
                val stateObjectEntries = stateObject.entries

                val typeStr = stateObjectEntries.elementAtOrNull(0)?.value?.toString() ?: continue
                val type = byName(typeStr) ?: continue
                val defEntry = stateObjectEntries.elementAtOrNull(1) ?: continue

                val defaultIdx: Int = if (defEntry.key != "def") {
                    Bullet.logger.warning("Invalid block mapping: ${defEntry.key}, could not find def, using 0")
                    0
                } else defEntry.value.jsonPrimitive.int

                val entriesMap = stateObjectEntries.elementAtOrNull(2)?.value?.jsonArray ?: continue
                for ((index, entry) in entriesMap.withIndex()) {
                    //val entryMap = JsonParser.parseString(entry.toString()).asJsonObject
                    val properties = entry.jsonObject.entries
                    //val compound = CompoundTag().fromJson(entryMap, 0, TagTypeRegistry())

                    val dataStringBuilder = StringBuilder()
                    val dataMap = HashMap<StateValue, Any>()

                    for ((propertyId, property) in properties) {
                        val state = StateValue.byName(propertyId) ?: continue
                        dataStringBuilder.append(state.name).append('=')
                        val value = property.jsonPrimitive

                        val parsed = state.parser(value.content.uppercase())
                        dataStringBuilder.append(parsed.toString().lowercase()).append(",")
                        dataMap[state] = parsed
                    }
                    val dataString = if (dataStringBuilder.isEmpty()) "" else "[${
                        dataStringBuilder.toString().substring(0, dataStringBuilder.length - 1)
                    }]"

                    val fullString = typeStr + dataString
                    val state = BlockState(type, dataMap, id)

                    if (defaultIdx == index) {
                        DEFAULT_STATES[type] = state
                    }

                    BY_STRING[fullString] = state
                    BY_ID[id] = state
                    INTO_STRING[state] = fullString
                    INTO_ID[state] = id

                    id++
                }
            }

        }
    }
}
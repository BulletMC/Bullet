package com.aznos.packets.data.block.states.type

import com.aznos.packets.newPacket.ResourceLocation

object StateTypes {

    private val ALL_STATE_TYPES = ArrayList<StateType>()
    private val BY_NAME = HashMap<String, StateType.Defined>()
    private val BY_ID = HashMap<Byte, MutableMap<Int, StateType.Defined>>()

    fun register(stateType: StateType) {
        ALL_STATE_TYPES += stateType
    }

    private var mappingIndex = 0

    private fun registerMapping(
        typeMap: HashMap<String, StateType.Defined>,
        typeIdMap: HashMap<Byte, MutableMap<Int, StateType.Defined>>,
        key: ResourceLocation,
        type: StateType
    ) {
        val defined = StateType.Defined(type, key, mappingIndex)
        mappingIndex++

        typeMap[defined.name.toString()] = defined

        val index: Int = mappingIndex
        val idMap = typeIdMap.computeIfAbsent(index.toByte()) { k: Byte? -> HashMap() }
        idMap[defined.id] = defined
    }

    fun byName(blockString: String): StateType? {
        val mapped = definedByName(blockString)
        return mapped?.stateType
    }

    fun definedByName(blockString: String): StateType.Defined? {
        return definedByName(ResourceLocation.fromString(blockString))
    }

    fun definedByName(blockKey: ResourceLocation): StateType.Defined? {
        return BY_NAME[blockKey.toString()]
    }
}
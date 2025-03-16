package com.aznos.registry

import com.aznos.packets.configuration.out.ServerConfigRegistryData
import com.aznos.packets.newPacket.ResourceLocation
import dev.dewy.nbt.tags.collection.CompoundTag

abstract class Registry<T>(val key: ResourceLocation) {

    var cachedNetworkPacket: ServerConfigRegistryData? = null; private set
    private val map = mutableMapOf<ResourceLocation, T>()
    private var locked: Boolean = false

    abstract fun asCompound(value: T): CompoundTag

    fun lock() {
        if (locked) return
        locked = true
        cachedNetworkPacket = ServerConfigRegistryData.fromRegistry(this)
    }

    private fun checkLocked() {
        check(!locked) {
            "Cannot operate on a locked registry"
        }
    }

    fun register(key: ResourceLocation, value: T): T {
        checkLocked()
        map[key] = value
        return value
    }

    fun unregister(key: ResourceLocation): Boolean {
        checkLocked()
        return map.remove(key) != null
    }

    fun lookup(key: ResourceLocation): T? = map[key]
    fun getMap(): Map<ResourceLocation, T> = map.toMap()
    fun getCompoundMap(): Map<ResourceLocation, CompoundTag> = map.mapValues { (_, value) -> asCompound(value) }

}
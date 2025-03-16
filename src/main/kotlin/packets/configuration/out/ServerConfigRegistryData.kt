package com.aznos.packets.configuration.out

import com.aznos.datatypes.CollectionType.writeCollection
import com.aznos.datatypes.NBTType.writeNbtCompound
import com.aznos.datatypes.OptionalType.writeOptional
import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.newPacket.Keyed
import com.aznos.packets.newPacket.ResourceLocation
import com.aznos.packets.newPacket.ServerPacket
import com.aznos.registry.Registry
import dev.dewy.nbt.tags.collection.CompoundTag

/**
 * This packet is sent when you want to apply registries to the client
 */
class ServerConfigRegistryData(
    var registryKey: ResourceLocation,
    var entries: List<RawEntry>
) : ServerPacket(key) {

    companion object {
        val key = Keyed(0x07, ResourceLocation.vanilla("configuration.out.registry_data"))

        fun fromRegistry(registry: Registry<*>): ServerConfigRegistryData {
            return registry.cachedNetworkPacket ?: ServerConfigRegistryData(
                registry.key,
                registry.getCompoundMap().map { RawEntry(it.key, it.value) }
            )
        }
    }

    override fun retrieveData(): ByteArray {
        return writeData {
            writeString(registryKey.toString())
            writeCollection(entries) { os, entry ->
                os.writeString(entry.id.toString())
                os.writeOptional(entry.value) { os1, value ->
                    os.writeNbtCompound(value)
                }
            }
        }
    }

    data class RawEntry(val id: ResourceLocation, val value: CompoundTag?) {
        constructor(nbt: CompoundTag) : this(
            ResourceLocation.fromString(nbt.getString("name").value),
            nbt.getCompound("element")
        )
    }

}
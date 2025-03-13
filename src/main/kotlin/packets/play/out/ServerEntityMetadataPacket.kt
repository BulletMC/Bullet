package com.aznos.packets.play.out

import com.aznos.datatypes.MetadataType
import com.aznos.datatypes.MetadataType.writeMetadata
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent when the server needs to update the metadata of an entity
 *
 * @param entityID The ID of the entity
 * @param metadata The metadata to update
 */
class ServerEntityMetadataPacket(
    entityID: Int,
    metadata: List<MetadataType.MetadataEntry>
) : Packet(0x44) {
    init {
        wrapper.writeVarInt(entityID)

        for(entry in metadata) {
            wrapper.writeMetadata(entry)
        }

        wrapper.writeByte(0xFF)
    }
}
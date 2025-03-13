package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.MetadataEntry
import com.aznos.packets.Packet

class ServerEntityMetadataPacket(
    entityID: Int,
    private val metadata: List<MetadataEntry>
) : Packet(0x44) {
    init {
        wrapper.writeVarInt(entityID)

        for(entry in metadata) {
            entry.write(wrapper)
        }
        wrapper.writeByte(0xFF)
    }
}
package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

class ServerDestroyEntitiesPacket(
    ids: IntArray
) : Packet(0x36) {
    init {
        wrapper.writeVarInt(ids.size)
        for(id in ids) {
            wrapper.writeVarInt(id)
        }
    }
}
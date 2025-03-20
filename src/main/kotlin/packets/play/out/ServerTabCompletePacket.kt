package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

class ServerTabCompletePacket(
    id: Int,
    start: Int,
    length: Int,
    matches: List<String>,
    tooltips: Map<String, String> = emptyMap()
) : Packet(0x0F) {
    init {
        wrapper.writeVarInt(id)
        wrapper.writeVarInt(start)
        wrapper.writeVarInt(length)
        wrapper.writeVarInt(matches.size)

        for (match in matches) {
            if (match.isBlank()) continue // Prevent empty strings

            wrapper.writeString(match)

            val hasTooltip = tooltips.containsKey(match)
            wrapper.writeBoolean(hasTooltip)

            if (hasTooltip) {
                wrapper.writeString(tooltips[match]!!)
            }
        }
    }
}
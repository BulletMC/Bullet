package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

/**
 * This is sent to the client when it should create a new scoreboard objective or remove one
 *
 * @param objectiveName A unique name for the objective
 * @param mode 0 to create the scoreboard, 1 to remove the scoreboard, 2 to update the text
 * @param value Only if mode is 0 or 2, the text to display on the scoreboard
 * @param type Only if mode is 0 or 2, 0 = "integer", 1 = "hearts"
 */
class ServerScoreboardObjectivePacket(
    objectiveName: String,
    mode: Byte,
    value: TextComponent? = null,
    type: Int? = null
) : Packet(0x4A) {
    init {
        wrapper.writeString(objectiveName)
        wrapper.writeByte(mode.toInt())

        if(value != null) wrapper.writeString(GsonComponentSerializer.gson().serialize(value))
        if(type != null) wrapper.writeVarInt(type)
    }
}
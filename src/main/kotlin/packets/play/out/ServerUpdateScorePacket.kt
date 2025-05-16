package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * This is sent to the client when it should update a scoreboard entry
 *
 * @param entityName For players this is their username, for other entities it's the UUID
 * @param action The action to perform, 0: create/update, 1: remove
 * @param objectiveName The name of the objective to update
 * @param value The score to be displayed next to the entry, only sent when action does not equal 1
 */
class ServerUpdateScorePacket(
    entityName: String,
    action: Byte,
    objectiveName: String,
    value: Int? = null
) : Packet(0x4D) {
    init {
        wrapper.writeString(entityName)
        wrapper.writeByte(action.toInt())
        wrapper.writeString(objectiveName)
        if(value != null) wrapper.writeVarInt(value)
    }
}
package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import com.aznos.packets.data.BossBarColor
import com.aznos.packets.data.BossBarDividers
import com.google.gson.JsonObject
import java.util.UUID

/**
 * This packet sends a boss bar to the client
 *
 * @param uuid The UUID of the boss bar
 * @param action The action to perform on the boss bar
 * @param title The title of the boss bar
 * @param health The health of the boss bar, 0-1
 * @param color The color of the boss bar
 * @param division The amount of notches in the boss bar
 * @param flags Optional flags, 0x1 - Darken sky, 0x2 - Dragon bar (used to play end music), 0x04 - Create fog
 */
class ServerBossBarPacket(
    uuid: UUID,
    action: Action,
    title: String? = null,
    health: Float? = null,
    color: BossBarColor? = null,
    division: BossBarDividers? = null,
    flags: Byte? = null
) : Packet(0x0C) {
    enum class Action(val id: Int) {
        ADD(0),
        REMOVE(1),
        UPDATE_HEALTH(2),
        UPDATE_TITLE(3),
        UPDATE_STYLE(4),
        UPDATE_FLAGS(5)
    }

    init {
        wrapper.writeUUID(uuid)
        wrapper.writeVarInt(action.id)

        when(action) {
            Action.ADD -> { //Add
                requireNotNull(title)
                requireNotNull(health)
                requireNotNull(color)
                requireNotNull(division)

                val jsonObj = JsonObject()
                jsonObj.addProperty("text", title)

                wrapper.writeString(jsonObj.toString())
                wrapper.writeFloat(health)
                wrapper.writeVarInt(color.id)
                wrapper.writeVarInt(division.id)

                if(flags != null) {
                    wrapper.writeByte(flags.toInt())
                }
            }
            Action.REMOVE -> { //Remove

            }
            Action.UPDATE_HEALTH -> { //Update health
                requireNotNull(health)
                wrapper.writeFloat(health)
            }
            Action.UPDATE_TITLE -> { //Update title
                requireNotNull(title)

                val jsonObj = JsonObject()
                jsonObj.addProperty("text", title)
                wrapper.writeString(jsonObj.toString())
            }
            Action.UPDATE_STYLE -> { //Update style
                requireNotNull(color)
                requireNotNull(division)

                wrapper.writeVarInt(color.id)
                wrapper.writeVarInt(division.id)
            }
            Action.UPDATE_FLAGS -> { //Update flags
                requireNotNull(flags)
                wrapper.writeByte(flags.toInt())
            }
        }
    }
}
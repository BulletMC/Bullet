package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.Packet

/**
 * This is sent to the client when it should display a scoreboard, a scoreboard is never
 * used in the notchian server, but it may be used for special purposes.
 */
class ServerDisplayScoreboardPacket(
    position: Byte,
    scoreName: String
) : Packet(0x43) {
    init {
        wrapper.writeByte(position.toInt())
        wrapper.writeString(scoreName)
    }
}
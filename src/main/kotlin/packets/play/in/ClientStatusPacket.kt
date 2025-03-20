package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent by the client when it respawns after dying or when
 * the client opens the statistics menu
 *
 * Action ID values:
 * 0 - Sent when the client respawns
 * 1- Sent when the client opens the statistics menu
 *
 * @property actionID The action ID of the packet (see above)
 */
class ClientStatusPacket(data: ByteArray) : Packet(data) {
    val actionID: Int = getIStream().readVarInt()
}
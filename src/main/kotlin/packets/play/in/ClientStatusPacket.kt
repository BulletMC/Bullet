package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent by the client when either the client is ready
 * to complete login or when the client is ready to respawn after death
 * for the action ID of 0
 *
 * It can also be called with action ID of 1 when the client opens the
 * statistics menu
 *
 * @property actionID The action ID of the packet (see above)
 */
class ClientStatusPacket(data: ByteArray) : Packet(data) {
    val actionID: Int = getIStream().readVarInt()
}
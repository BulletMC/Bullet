package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * Sent by the client as confirmation of the player position and look packet
 */
@Suppress("unused")
class ClientTeleportConfirmPacket(data: ByteArray) : Packet(data) {
    val teleportID: Int = getIStream().readVarInt()
}
package com.aznos.packets.login.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.packets.Packet

/**
 * Packet representing a login start packet from the client
 * The server sends back a login success packet
 *
 * @property username The username of the player joining
*/
class ClientLoginStartPacket(data: ByteArray) : Packet(data) {
    var username: String = getIStream().readString()
}


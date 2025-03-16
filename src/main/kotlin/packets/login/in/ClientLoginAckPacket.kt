package com.aznos.packets.login.`in`

import com.aznos.packets.Packet

/**
 * Send when the client acknowledged the LoginSuccess packet from the server.
 */
class ClientLoginAckPacket(data: ByteArray) : Packet(data)
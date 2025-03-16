package com.aznos.packets.configuration.`in`

import com.aznos.packets.Packet

/*
 * Send when the client acknowledged the Config Finish packet from the server.
 */
class ClientConfigFinishAckPacket(data: ByteArray) : Packet(data)
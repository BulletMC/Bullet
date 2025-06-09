package com.aznos.packets.login.`in`.packets.login.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * This packet is sent by the client in response to a server encryption request
 */
class ClientEncryptionResponsePacket(data: ByteArray) : Packet(data) {
    var secretKey: ByteArray
    var verifyToken: ByteArray

    init {
        val input = getIStream()

        val secretLen = input.readVarInt()
        secretKey = ByteArray(secretLen)
        input.readFully(secretKey)

        val verifyTokenLen = input.readVarInt()
        verifyToken = ByteArray(verifyTokenLen)
        input.readFully(verifyToken)
    }
}
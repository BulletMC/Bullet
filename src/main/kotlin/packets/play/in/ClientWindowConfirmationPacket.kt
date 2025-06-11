package com.aznos.packets.play.`in`

import com.aznos.packets.Packet

/**
 * If a confirmation sent by the client was not accepted, the server will reply with
 * a [com.aznos.packets.play.out.ServerWindowConfirmationPacket] packet
 * with the Accepted field set to false. When this happens, the client must send this packet
 * to apologize (as with movement); otherwise the server ignores any successive confirmations.
 *
 * @param windowID The ID of the window that the action occurred in
 * @param actionNumber Every action that is to be accepted has a unique number
 * @param accepted Whether the action was accepted or not
 */
class ClientWindowConfirmationPacket(data: ByteArray) : Packet(data) {
    val windowID: Byte
    val actionNumber: Short
    val accepted: Boolean

    init {
        val input = getIStream()

        windowID = input.readByte()
        actionNumber = input.readShort()
        accepted = input.readBoolean()
    }
}
package com.aznos.packets.play.out

import com.aznos.packets.Packet

/**
 * A packet from the server indicating whether a request from the client was accepted
 * or whether there was a conflict (due to lag). If the packet was not accepted, the client
 * must respond with a Server-Bound window confirmation packet.
 *
 * @param windowID The ID of the window that the action occurred in
 * @param actionNumber Every action that is to be accepted has a unique number
 * @param accepted Whether the action was accepted or not
 */
class ServerWindowConfirmationPacket(
    windowID: Byte,
    actionNumber: Short,
    accepted: Boolean
) : Packet(0x11) {
    init {
        wrapper.writeByte(windowID.toInt())
        wrapper.writeShort(actionNumber.toInt())
        wrapper.writeBoolean(accepted)
    }
}
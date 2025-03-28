package com.aznos.packets.play.out

import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Sent when the client has placed a sign and is allowed to send an Update Sign packet.
 * There must already be a sign at the given location (which the client does not do automatically)
 * so you have to send a Block Change packet first
 *
 * @param blockPos The position of the sign
 */
class ServerOpenSignEditorPacket(
    blockPos: Position
) : Packet(0x2E) {
    init {
        wrapper.writeDouble(blockPos.x)
        wrapper.writeDouble(blockPos.y)
        wrapper.writeDouble(blockPos.z)
    }
}
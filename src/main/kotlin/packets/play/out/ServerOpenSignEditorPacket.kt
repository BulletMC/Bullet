package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.packets.Packet

/**
 * Sent when the client has placed a sign and is allowed to send an Update Sign packet.
 * There must already be a sign at the given location (which the client does not do automatically)
 * so you have to send a Block Change packet first
 *
 * @param blockPos The position of the sign
 */
class ServerOpenSignEditorPacket(
    blockPos: BlockPositionType.BlockPosition
) : Packet(0x2E) {
    init {
        wrapper.writeBlockPos(blockPos)
    }
}
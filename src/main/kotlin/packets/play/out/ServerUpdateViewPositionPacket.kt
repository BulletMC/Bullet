package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Sent whenever the player moves across a chunk border horizontally and also
 * for any integer change in the vertical axis
 *
 * This is used to determine what chunks should remain loaded and if a chunk
 * load should be ignored, chunks outside the view distance should be unloaded
 */
class ServerUpdateViewPositionPacket(
    x: Int,
    z: Int
) : Packet(0x40) {
    init {
        wrapper.writeVarInt(x)
        wrapper.writeVarInt(z)
    }
}
package com.aznos.packets.play.`in`

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * Sent to the server when a player starts/stops digging a block
 *
 * @property status The action the player is taking
 * @property blockPos The block position
 * @property face The face being hit
 */
class ClientDiggingPacket(data: ByteArray) : Packet(data) {
    val status: Int
    val blockPos: BlockPositionType.BlockPosition
    val face: Int

    init {
        val input = getIStream()
        status = input.readVarInt()

        blockPos = BlockPositionType.BlockPosition(input.readLong())

        face = input.readByte().toInt()
    }
}
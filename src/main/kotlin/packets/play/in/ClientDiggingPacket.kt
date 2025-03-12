package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Sent to the server when a player starts/stops digging a block.
 *
 * Status can be one of seven values
 * 0: Started digging
 * 1: Cancelled digging
 * 2: Finished digging
 * 3: Drop item stack
 * 4: Drop item
 * 5: Shoot arrow/finish eating
 * 6: Swap item in hand
 *
 * @property status The action the player is taking (see above)
 * @property location The block position
 * @property face The face being hit
 */
class ClientDiggingPacket(data: ByteArray) : Packet(data) {
    val status: Int
    val location: Position
    val face: Int

    init {
        val input = getIStream()
        status = input.readVarInt()

        val blockPosition = input.readLong()
        val x = (blockPosition shr 38).toInt()
        val y = (blockPosition and 0xFFF).toInt()
        val z = (blockPosition shl 26 shr 38).toInt()
        location = Position(x, y, z)

        face = input.readByte().toInt()
    }
}
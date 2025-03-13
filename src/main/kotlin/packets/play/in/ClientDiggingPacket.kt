package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Sent to the server when a player starts/stops digging a block
 *
 * @property status The action the player is taking
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
        val x = (blockPosition shr 38).toDouble()
        val y = (blockPosition and 0xFFF).toDouble()
        val z = (blockPosition shl 26 shr 38).toDouble()
        location = Position(x, y, z)

        face = input.readByte().toInt()
    }
}
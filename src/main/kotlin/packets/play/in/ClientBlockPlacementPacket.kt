package com.aznos.packets.play.`in`

import com.aznos.datatypes.VarInt
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.Location
import com.aznos.entity.player.data.Position
import com.aznos.packets.Packet

/**
 * Packet sent by the client when placing a block
 *
 * @property hand The hand used to place the block (0 for main hand, 1 for off-hand)
 * @property location The location of the block being placed
 * @property face The face of the block being placed (0-5)
 * @property cursorPositionX The X position of the crosshair on the block, 0-1
 * @property cursorPositionY The Y position of the crosshair on the block, 0-1
 * @property cursorPositionZ The Z position of the crosshair on the block, 0-1
 * @property insideBlock Whether the players head is inside a block
 */
class ClientBlockPlacementPacket(data: ByteArray) : Packet(data) {
    val hand: Int
    val location: Position
    val face: Int
    val cursorPositionX: Float
    val cursorPositionY: Float
    val cursorPositionZ: Float
    val insideBlock: Boolean

    init {
        val input = getIStream()
        hand = input.readVarInt()

        val blockPosition = input.readLong()
        location = Position(
            (blockPosition shr 38).toInt(),
            (blockPosition and 0xFFF).toInt(),
            ((blockPosition shl 26) shr 38).toInt()
        )

        face = input.readVarInt()
        cursorPositionX = input.readFloat()
        cursorPositionY = input.readFloat()
        cursorPositionZ = input.readFloat()
        insideBlock = input.readBoolean()
    }
}
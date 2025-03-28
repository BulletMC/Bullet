package com.aznos.packets.play.out

import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.packets.Packet
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag

/**
 * Sets the block entity associated with the block at the given position
 *
 * Available actions:
 * 1 -> Set data of a mob spawner
 * 2 -> Set command block tet
 * 3 -> Set the level, primary, and secondary powers of a beacon
 * 4 -> Set rotation and skin of a mob head
 * 5 -> Declare a conduit
 * 6 -> Set the base color and patterns of a banner
 * 7 -> Set the data for a structure tile entity
 * 8 -> Set the destination for an end gateway
 * 9 -> Set the text on a sign
 * 10 -> Unused
 * 11 -> Declare a bed
 * 12 -> Set data of a jigsaw block
 * 13 -> Set items in a campfire
 * 14 -> Beehive information
 *
 * @param blockPos The position of the block
 * @param action The action to perform, see above
 * @param nbtData Data to set, may be a TAG_END(0) in which case the block at the
 * given position is removed
 */
class ServerBlockEntityDataPacket(
    blockPos: BlockPositionType.BlockPosition,
    action: Byte,
    nbtData: CompoundTag
) : Packet(0x09) {
    init {
        wrapper.writeBlockPos(blockPos)
        wrapper.writeByte(action.toInt())

        val nbt = Nbt()
        nbt.toStream(nbtData, wrapper)
    }
}
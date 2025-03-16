package com.aznos.datatypes

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/**
 * Represents a slot in the players inventory
 */
object Slot {
    /**
     * Represents slot data in the player's inventory
     *
     * @param present Whether the slot is present
     * @param itemID The item ID of the slot
     * @param itemCount How many of the item is in that slot
     * @param nbt Related NBT data
     */
    data class SlotData(
        var present: Boolean,
        val itemID: Int? = null,
        var itemCount: Byte? = null,
        var nbt: CompoundTag? = null
    )

    /**
     * Reads a slot from the [DataInputStream]
     *
     * @return The slot data for the given slot
     * @throws IOException If an I/O error occurs
     */
    @Throws(IOException::class)
    fun DataInputStream.readSlot(): SlotData {
        val present = readBoolean()
        if(!present) return SlotData(false)

        val itemID = readVarInt()
        val itemCount = readByte()

        val nbt = if (readByte().toInt() == 0) null
        else {
            reset()
            Nbt().fromStream(this)
        }

        return SlotData(true, itemID, itemCount, nbt)
    }


    /**
     * Writes a slot to the [DataOutputStream]
     *
     * @param slot The slot data to be written
     * @throws IOException If an I/O error occurs
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeSlot(slot: SlotData) {
        writeBoolean(slot.present)
        if(!slot.present) return

        writeVarInt(slot.itemID!!)
        writeByte(slot.itemCount!!.toInt())

        if(slot.nbt == null) {
            writeByte(0)
        } else {
            Nbt().toStream(slot.nbt!!, this)
        }
    }
}
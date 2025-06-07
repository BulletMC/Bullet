package com.aznos.datatypes

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import net.querz.nbt.io.NBTInputStream
import net.querz.nbt.io.NBTOutputStream
import net.querz.nbt.tag.CompoundTag
import net.querz.nbt.tag.Tag
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

    fun SlotData.toItemStack(): ItemStack =
        if(!present) ItemStack.of(Item.AIR)
        else ItemStack(
            item = Item.getItemFromID(itemID!!) ?: Item.AIR,
            count = itemCount?.toInt() ?: 0,
            nbt = nbt
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

        mark(512)
        val tagId = readByte()
        val nbt: CompoundTag? = if(tagId.toInt() == 0) {
            null
        } else {
            reset()
            //TODO: fix this warning
            NBTInputStream(this).use { nbtIn ->
                val tag = nbtIn.readTag(Tag.DEFAULT_MAX_DEPTH)
                if(tag is CompoundTag) tag
                else throw IOException(
                    "Expected CompoundTag in slot but got ${tag::class.simpleName}"
                )
            }
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
            NBTOutputStream(this).writeTag(slot.nbt, Tag.DEFAULT_MAX_DEPTH)
        }
    }
}
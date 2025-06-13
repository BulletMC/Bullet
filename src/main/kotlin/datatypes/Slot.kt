package com.aznos.datatypes

import com.aznos.Bullet
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
import java.io.PushbackInputStream

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

    fun SlotData.toItemStack(): ItemStack {
        if(!present) return ItemStack.empty()

        val id = itemID ?: return ItemStack.empty()
        val count = itemCount?.toInt() ?: 1

        val known = Item.getItemFromID(id)
        return if(known != null) {
            ItemStack(known, count)
        } else {
            ItemStack.opaque(id, count)
        }
    }

    /**
     * Reads a slot from the [DataInputStream]
     *
     * @return The slot data for the given slot
     * @throws IOException If an I/O error occurs
     */
    @Throws(IOException::class)
    fun DataInputStream.readSlot(): Slot.SlotData {
        val present = readBoolean()
        if(!present) return Slot.SlotData(false)

        val itemID    = readVarInt()
        val itemCount = readByte()

        val first = readUnsignedByte()
        val nbt: CompoundTag? =
            if(first == 0) null
            else {
                val pb = PushbackInputStream(this, 1)
                pb.unread(first)

                val named = NBTInputStream(pb).use { it.readTag(Tag.DEFAULT_MAX_DEPTH) }
                (named.tag as? CompoundTag)
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
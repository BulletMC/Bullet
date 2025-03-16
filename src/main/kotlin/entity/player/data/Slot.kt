package com.aznos.entity.player.data

import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.datatypes.VarInt.writeVarInt
import dev.dewy.nbt.Nbt
import dev.dewy.nbt.tags.collection.CompoundTag
import java.io.DataInputStream
import java.io.DataOutputStream

data class Slot(
    var present: Boolean,
    val itemID: Int? = null,
    var itemCount: Byte? = null,
    var nbt: CompoundTag? = null
) {
    companion object {
        fun read(input: DataInputStream): Slot {
            val present = input.readBoolean()
            if(!present) return Slot(false)

            val itemID = input.readVarInt()
            val itemCount = input.readByte()

            val nbt = if(input.readByte().toInt() == 0) null
            else {
                input.reset()
                val nbtData = Nbt().fromStream(input)
                nbtData
            }

            return Slot(true, itemID, itemCount, nbt)
        }

        fun write(output: DataOutputStream, slot: Slot) {
            output.writeBoolean(slot.present)
            if(!slot.present) return

            output.writeVarInt(slot.itemID!!)
            output.writeByte(slot.itemCount!!.toInt())

            if(slot.nbt == null) {
                output.writeByte(0)
            } else {
                Nbt().toStream(slot.nbt!!, output)
            }
        }
    }
}

package com.aznos.entity.player.data

import com.aznos.datatypes.VarInt.writeVarInt
import java.io.DataOutputStream

data class MetadataEntry(
    val index: Byte,
    val type: Int,
    val value: Any
) {
    fun write(output: DataOutputStream) {
        output.writeByte(index.toInt())
        output.writeVarInt(type)

        when(type) {
            0 -> output.writeByte(value as Int)
            1 -> output.writeVarInt(value as Int)
            2 -> output.writeFloat(value as Float)
            3 -> output.writeUTF(value as String)
            7 -> output.writeBoolean(value as Boolean)
            18 -> output.writeVarInt(value as Int)
        }
    }
}

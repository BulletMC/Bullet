package com.aznos.datatypes

import com.aznos.datatypes.BlockPositionType.writeBlockPos
import com.aznos.datatypes.VarInt.writeVarInt
import java.io.DataOutputStream
import java.io.IOException

/**
 *
 */
object MetadataType {
    /**
     * Represents a metadata entry
     *
     *  @param index Unique index key determining the meaning of the following values
     *  if this is 0xFF it's the end of the metadata
     *  @param type Only if index is not 0xFF
     *  @param value Only if index is not 0xFF
     */
    data class MetadataEntry(val index: Byte, val type: Int, val value: Any)

    /**
     * Writes a metadata entry to the output stream
     *
     * @throws IOException If an I/O error occurs while reading the input stream
     * @see <a href="https://web.archive.org/web/20201217235347/https://wiki.vg/Entity_metadata">Entity Metadata</a>
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeMetadata(entry: MetadataEntry) {
        writeByte(entry.index.toInt())
        writeVarInt(entry.type)

        when(entry.type) {
            0 -> writeByte(entry.value as Int)
            1 -> writeVarInt(entry.value as Int)
            2 -> writeFloat(entry.value as Float)
            3 -> writeUTF(entry.value as String)
            7 -> writeBoolean(entry.value as Boolean)
            10 -> {
                val (isPresent, pos) = entry.value as Pair<Boolean, BlockPositionType.BlockPosition?>
                writeBoolean(isPresent)
                if(isPresent && pos != null) {
                    writeBlockPos(pos)
                }
            }
            18 -> writeVarInt(entry.value as Int)
        }
    }
}
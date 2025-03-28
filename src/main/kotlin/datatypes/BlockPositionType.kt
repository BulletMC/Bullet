package com.aznos.datatypes

import kotlinx.serialization.Serializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

object BlockPositionType {
    /**
     * Represents a block position in the world
     */
    @Serializable
    data class BlockPosition(
        var x: Double,
        var y: Double,
        var z: Double,
    ) {

        constructor(blockPos: Long) : this(
            (blockPos shr 38).toDouble(),
            (blockPos and 0xFFF).toDouble(),
            (blockPos shl 26 shr 38).toDouble(),
        )

        fun add(x: Double, y: Double, z: Double): BlockPosition {
            this.x += x
            this.y += y
            this.z += z

            return this
        }
    }


    /**
     * Reads a block position from the [DataInputStream]
     *
     * @return Decoded block position
     * @throws IOException If an I/O error occurs while reading the input stream
     */
    @Throws(IOException::class)
    fun DataInputStream.readBlockPos(): BlockPosition {
        val x = readDouble()
        val y = readDouble()
        val z = readDouble()

        return BlockPosition(x, y, z)
    }

    /**
     * Writes a block position to the [DataOutputStream]
     *
     * @param blockPos Block position to write
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeBlockPos(blockPos: BlockPosition) {
        val blockPosition: Long = ((blockPos.x.toLong() and 0x3FFFFFF) shl 38) or
                ((blockPos.z.toLong() and 0x3FFFFFF) shl 12) or
                (blockPos.y.toLong() and 0xFFF)

        writeLong(blockPosition)
    }

    /**
     * Writes a block position to the [DataOutputStream] and just writes
     * X, Y, and Z as doubles and doesn't do any bit shifting
     *
     * @param blockPos Block position to write
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeVanillaBlockPos(blockPos: BlockPosition) {
        writeDouble(blockPos.x)
        writeDouble(blockPos.y)
        writeDouble(blockPos.z)
    }
}
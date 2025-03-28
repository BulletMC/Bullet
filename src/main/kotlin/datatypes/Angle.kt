package com.aznos.datatypes

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

object Angle {
    /**
     * Reads an angle from the [DataInputStream]
     *
     * @return Decoded angle
     * @throws IOException If an I/O error occurs while reading the input stream
     */
    @Throws(IOException::class)
    fun DataInputStream.readAngle(): Float {
        return readByte().toFloat() * 360.0f / 256.0f
    }

    /**
     * Writes an angle to the [DataOutputStream]
     *
     * @param angle The angle to encode
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeAngle(angle: Float) {
        writeByte((angle * 256.0f / 360.0f).toInt())
    }
}
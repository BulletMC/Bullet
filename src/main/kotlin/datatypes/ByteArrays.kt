package com.aznos.datatypes

import java.io.IOException
import java.io.OutputStream

object ByteArrays {
    /**
     * Writes the given byte array to the [java.io.OutputStream].
     *
     * @param src The byte array to write
     * @throws java.io.IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun OutputStream.writeBytes(src: ByteArray) {
        write(src)
    }
}
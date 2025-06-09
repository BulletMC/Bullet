package com.aznos.datatypes.datatypes

import java.io.IOException
import java.io.OutputStream
import kotlin.jvm.Throws

object ByteArrays {
    /**
     * Writes the given byte array to the [OutputStream].
     *
     * @param src The byte array to write
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun OutputStream.writeBytes(src: ByteArray) {
        write(src)
    }
}
package com.aznos.packets

import com.aznos.ClientSession
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.Player
import java.io.*


/**
 * Base class for all packets
 *
 * @param data The raw packet data
 */
@Suppress("TooManyFunctions")
open class Packet(
    private val data: ByteArray
) : DataOutput {
    private val buffer = ByteArrayOutputStream()
    val wrapper = DataOutputStream(buffer)

    /**
     * The constructor for client-bound packets
     */
    constructor(id: Int) : this(byteArrayOf()) {
        wrapper.writeVarInt(id)
    }

    init {
        wrapper.write(data)
        wrapper.flush()
    }

    /**
     * @return a [DataInputStream] wrapping the packet data
     */
    fun getIStream(): DataInputStream {
        return DataInputStream(ByteArrayInputStream(buffer.toByteArray()))
    }

    open fun apply(client: ClientSession) {}

    /**
     * Retrieves the complete packet data with its length prefixed
     *
     * @return The full packet as a byte array
     */
    fun retrieveData(): ByteArray {
        val raw = buffer.toByteArray()
        val buffer = ByteArrayOutputStream()

        buffer.writeVarInt(raw.size)
        buffer.write(raw)

        return buffer.toByteArray()
    }

    //Data output specific functions
    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        wrapper.write(b)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        wrapper.write(b, off, len)
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        wrapper.write(b)
    }

    @Throws(IOException::class)
    override fun writeBoolean(v: Boolean) {
        wrapper.writeBoolean(v)
    }

    @Throws(IOException::class)
    override fun writeByte(v: Int) {
        wrapper.writeByte(v)
    }

    @Throws(IOException::class)
    override fun writeBytes(s: String) {
        wrapper.writeBytes(s)
    }

    @Throws(IOException::class)
    override fun writeChar(v: Int) {
        wrapper.writeChar(v)
    }

    @Throws(IOException::class)
    override fun writeChars(s: String) {
        wrapper.writeChars(s)
    }

    @Throws(IOException::class)
    override fun writeDouble(v: Double) {
        wrapper.writeDouble(v)
    }

    @Throws(IOException::class)
    override fun writeFloat(v: Float) {
        wrapper.writeFloat(v)
    }

    @Throws(IOException::class)
    override fun writeInt(v: Int) {
        wrapper.writeInt(v)
    }

    @Throws(IOException::class)
    override fun writeLong(v: Long) {
        wrapper.writeLong(v)
    }

    @Throws(IOException::class)
    override fun writeShort(v: Int) {
        wrapper.writeShort(v)
    }

    @Throws(IOException::class)
    override fun writeUTF(s: String) {
        wrapper.writeUTF(s)
    }
}
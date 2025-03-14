package com.aznos.packets.play.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.packets.Packet

/**
 * Must have at least op level 2 to use, appears to only be used in single-player
 *
 * @property locked Whether the difficulty is locked
 */
@Suppress("unused")
class ClientPluginMessage(data: ByteArray) : Packet(data) {
    val channel: String
    val pluginData: ByteArray

    init {
        val input = getIStream()

        channel = input.readString()
        pluginData = ByteArray(input.available())
    }
}
package com.aznos.packets.play.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.packets.Packet

/**
 * Mods and plugins can use this to send their data, minecraft uses some plugin channels
 *
 * @property channel Name of the plugin channel used to send the data
 * @property pluginData Any data
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
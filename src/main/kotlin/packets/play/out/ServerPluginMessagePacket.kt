package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet

/**
 * Mods and plugins can use this to send their data, minecraft uses some plugin channels
 *
 * @param channel Name of the plugin channel used to send the data
 * @param pluginData Any data
 */
class ServerPluginMessagePacket(
    channel: String,
    data: ByteArray
) : Packet(0x17) {
    init {
        wrapper.writeString(channel)

        wrapper.writeVarInt(data.size)
        wrapper.writeBytes(data.toString())
    }
}
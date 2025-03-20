package com.aznos.packets.login.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.Packet
import com.google.gson.JsonObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

/**
 * This packet is sent when you want to disconnect the client during the LOGIN phase
 *
 * @param message The reason for the disconnection
*/
class ServerLoginDisconnectPacket(message: Component) : Packet(0x00) {
    init {
        wrapper.writeString(JSONComponentSerializer.json().serialize(message))
    }
}

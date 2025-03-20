package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.Packet
import com.google.gson.JsonObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

/**
 * This packet is used to disconnect the client whenever the game is in the play state
 */
class ServerPlayDisconnectPacket(
    message: Component
) : Packet(0x19) {
    init {
        wrapper.writeString(JSONComponentSerializer.json().serialize(message))
    }
}
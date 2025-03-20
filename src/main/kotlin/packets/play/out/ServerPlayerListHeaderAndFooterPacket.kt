package com.aznos.packets.play.out

import com.aznos.Bullet
import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.Packet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

/**
 * This packet may be used to display additional information above/below the player list
 *
 * @param header What to display on the header component,
 * make this an empty component to remove it
 * @param footer What to display on the footer component,
 * make this an empty component to remove it
 */
class ServerPlayerListHeaderAndFooterPacket(
    header: Component,
    footer: Component
) : Packet(0x53) {
    init {
        wrapper.writeString(JSONComponentSerializer.json().serialize(header))
        wrapper.writeString(JSONComponentSerializer.json().serialize(footer))
    }
}
package com.aznos.packets.play.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

/**
 * This packet is used to send either action bar or titles to the client
 *
 * @param action The action to perform, see [TitleAction]
 * @param text The text to set
 * @param fadeInTicks The ticks it takes to fade in the title
 * @param stayTicks The ticks the title stays on screen
 * @param fadeOutTicks The ticks it takes to fade out the title
 */
class ServerTitlePacket(
    action: TitleAction,
    text: TextComponent = Component.empty(),
    fadeInTicks: Int = 5,
    stayTicks: Int = 20,
    fadeOutTicks: Int = 5
) : Packet(0x4F) {
    init {
        wrapper.writeVarInt(action.ordinal)

        when(action) {
            TitleAction.SET_TITLE -> {
                wrapper.writeString(GsonComponentSerializer.gson().serialize(text))
            }

            TitleAction.SET_SUBTITLE -> {
                wrapper.writeString(GsonComponentSerializer.gson().serialize(text))
            }

            TitleAction.SET_ACTIONBAR -> {
                wrapper.writeString(GsonComponentSerializer.gson().serialize(text))
            }

            TitleAction.SET_TIME_AND_DISPLAY -> {
                wrapper.writeVarInt(fadeInTicks)
                wrapper.writeVarInt(stayTicks)
                wrapper.writeVarInt(fadeOutTicks)
            }

            TitleAction.HIDE -> {}
            TitleAction.RESET -> {}
        }
    }
}

enum class TitleAction {
    SET_TITLE,
    SET_SUBTITLE,
    SET_ACTIONBAR,
    SET_TIME_AND_DISPLAY,
    HIDE,
    RESET
}
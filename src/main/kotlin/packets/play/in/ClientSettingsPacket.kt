package com.aznos.packets.play.`in`

import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.packets.Packet

/**
 * Packet sent by the client to inform the server about the client settings
 *
 * @property locale The language locale of the client (e.g. "en_US")
 * @property viewDistance The client's view distance setting
 * @property chatMode 0: Enabled, 1: Commands only, 2: Hidden
 * @property chatColors Whether the client has chat colors enabled
 * @property displayedSkinParts A bitmask representing which parts of the player's skin are displayed
 * @property mainHand 0: Left, 1: Right
 */
class ClientSettingsPacket(data: ByteArray) : Packet(data) {
    val locale: String
    val viewDistance: Byte
    val chatMode: Int
    val chatColors: Boolean
    val displayedSkinParts: Byte
    val mainHand: Int

    init {
        val input = getIStream()

        locale = input.readString()
        viewDistance = input.readByte()
        chatMode = input.readVarInt()
        chatColors = input.readBoolean()
        displayedSkinParts = input.readByte()
        mainHand = input.readVarInt()
    }

    fun isCapeEnabled(): Boolean = (displayedSkinParts.toInt() and 0x01) != 0
    fun isJacketEnabled(): Boolean = (displayedSkinParts.toInt() and 0x02) != 0
    fun isLeftSleeveEnabled(): Boolean = (displayedSkinParts.toInt() and 0x04) != 0
    fun isRightSleeveEnabled(): Boolean = (displayedSkinParts.toInt() and 0x08) != 0
    fun isLeftPantsLegEnabled(): Boolean = (displayedSkinParts.toInt() and 0x10) != 0
    fun isRightPantsLegEnabled(): Boolean = (displayedSkinParts.toInt() and 0x20) != 0
    fun isHatEnabled(): Boolean = (displayedSkinParts.toInt() and 0x40) != 0
}
package com.aznos.packets.play.`in`

import com.aznos.packets.Packet

/**
 * Must have at least op level 2 to use, appears to only be used in single-player
 *
 * @property locked Whether the difficulty is locked
 */
@Suppress("unused")
class ClientLockDifficultyPacket(data: ByteArray) : Packet(data) {
    private val locked: Boolean = getIStream().readBoolean()
}
package com.aznos.packets.play.out

import com.aznos.packets.Packet

/**
 * Packet sent by the server to update the client about the world time
 * Time is based on ticks, with 20 ticks in one second, there are 24000 ticks in a day
 * This makes minecraft days exactly 20 minutes long
 *
 * The time of day is based on the timestamp module 24000
 * 0 is sunrise, 6000 is noon, 12000 is sunset, and 18000 is midnight
 */
class ServerTimeUpdatePacket(
    worldAge: Long,
    timeOfDay: Long
) : Packet(0x4E) {
    init {
        wrapper.writeLong(worldAge)
        wrapper.writeLong(timeOfDay)
    }
}
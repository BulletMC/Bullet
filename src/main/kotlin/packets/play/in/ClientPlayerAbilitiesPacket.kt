package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerPlayerAbilitiesPacket
import kotlin.experimental.and

/**
 * The vanilla client sends this packet when the player starts/stops flying
 * with the flags parameter changed accordingly, with the flag being 0x02 of
 * is flying
 */
class ClientPlayerAbilitiesPacket(data: ByteArray) : Packet(data) {
    val flags: Byte = getIStream().readByte()

    override fun apply(client: ClientSession) {
        if(client.player.canFly) {
            val flying = (flags and 0x02).toInt() == 0x02
            client.player.isFlying = flying
        } else {
            client.player.isFlying = false
            client.player.sendPacket(
                ServerPlayerAbilitiesPacket(
                    0,
                    0f,
                )
            )
        }
    }
}
package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.config.AcknowledgeFinishConfiguration
import com.maddoxh.bullet.network.packet.impl.`in`.config.ClientInformation
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundKnownPacks
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundPluginMessage
import com.maddoxh.bullet.network.packet.impl.`in`.handshake.HandshakeIntention
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginAcknowledged
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginStart
import com.maddoxh.bullet.network.packet.impl.`in`.status.PingRequest
import com.maddoxh.bullet.network.packet.impl.`in`.status.StatusRequest
import com.maddoxh.bullet.state.ConnectionState

object PacketReader {
    fun read(
        state: ConnectionState,
        packetID: Int,
        payloadLength: Int,
        input: MinecraftInputStream
    ): InboundPacket? = when(state) {
        ConnectionState.HANDSHAKE -> when(packetID) {
            0x00 -> HandshakeIntention.decode(input)
            else -> skip(input, payloadLength)
        }
        ConnectionState.STATUS -> when(packetID) {
            0x00 -> StatusRequest
            0x01 -> PingRequest.decode(input)
            else -> skip(input, payloadLength)
        }
        ConnectionState.LOGIN -> when(packetID) {
            0x00 -> LoginStart.decode(input)
            0x03 -> LoginAcknowledged
            else -> skip(input, payloadLength)
        }
        ConnectionState.CONFIGURATION -> when(packetID) {
            0x00 -> ClientInformation.decode(input)
            0x02 -> ServerboundPluginMessage.decode(input, payloadLength)
            0x03 -> AcknowledgeFinishConfiguration
            0x07 -> ServerboundKnownPacks.decode(input)
            else -> skip(input, payloadLength)
        }
        else -> skip(input, payloadLength)
    }

    private fun skip(input: MinecraftInputStream, len: Int): InboundPacket? {
        input.skipNBytes(len.toLong())
        return null
    }
}

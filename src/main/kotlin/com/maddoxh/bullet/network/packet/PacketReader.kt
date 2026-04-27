package com.maddoxh.bullet.network.packet

import com.maddoxh.bullet.io.MinecraftInputStream
import com.maddoxh.bullet.io.VarInt
import com.maddoxh.bullet.network.packet.impl.`in`.handshake.HandshakeIntention
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.config.AcknowledgeFinishConfiguration
import com.maddoxh.bullet.network.packet.impl.`in`.config.ClientInformation
import com.maddoxh.bullet.network.packet.impl.`in`.config.KnownPack
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundKnownPacks
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundPluginMessage
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginAcknowledged
import com.maddoxh.bullet.network.packet.impl.`in`.login.LoginStart
import com.maddoxh.bullet.network.packet.impl.`in`.status.PingRequest
import com.maddoxh.bullet.network.packet.impl.`in`.status.StatusRequest
import com.maddoxh.bullet.state.ConnectionState
import java.util.UUID

object PacketReader {
    fun read(
        state: ConnectionState,
        packetID: Int,
        payloadLength: Int,
        input: MinecraftInputStream
    ): InboundPacket? = when(state) {
        ConnectionState.HANDSHAKE     -> readHandshake(packetID, input)
        ConnectionState.STATUS        -> readStatus(packetID, payloadLength, input)
        ConnectionState.LOGIN         -> readLogin(packetID, payloadLength, input)
        ConnectionState.CONFIGURATION -> readConfiguration(packetID, payloadLength, input)

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }

    private fun readHandshake(packetID: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> HandshakeIntention(
            protocolVersion = input.readVarInt(),
            serverAddress = input.readMCString(),
            serverPort = input.readUnsignedShort(),
            nextState = input.readVarInt()
        )

        else -> null
    }

    private fun readStatus(packetID: Int, payloadLength: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> StatusRequest
        0x01 -> PingRequest(payload = input.readLong())

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }

    private fun readLogin(packetID: Int, payloadLength: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> LoginStart(
            name = input.readMCString(),
            uuid = UUID(input.readLong(), input.readLong())
        )

        0x03 -> LoginAcknowledged

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }

    private fun readConfiguration(packetID: Int, payloadLength: Int, input: MinecraftInputStream): InboundPacket? = when(packetID) {
        0x00 -> ClientInformation(
            locale              = input.readMCString(),
            viewDistance        = input.readByte(),
            chatMode            = input.readVarInt(),
            chatColors          = input.readBoolean(),
            displayedSkinParts  = input.readUnsignedByte(),
            mainHand            = input.readVarInt(),
            enableTextFiltering = input.readBoolean(),
            allowServerListings = input.readBoolean(),
            particleStatus      = input.readVarInt(),
        )

        0x02 -> {
            val channel = input.readMCString()
            val dataLen = payloadLength - channel.toByteArray().size - VarInt.varIntSize(channel.toByteArray().size)
            val data = ByteArray(dataLen.coerceAtLeast(0))
            if(data.isNotEmpty()) input.readFully(data)

            ServerboundPluginMessage(channel, data)
        }

        0x03 -> AcknowledgeFinishConfiguration
        0x07 -> {
            val count = input.readVarInt()
            val packs = (0 until count).map {
                KnownPack(
                    namespace = input.readMCString(),
                    id        = input.readMCString(),
                    version   = input.readMCString()
                )
            }

            ServerboundKnownPacks(packs)
        }

        else -> {
            input.skipNBytes(payloadLength.toLong())
            null
        }
    }
}

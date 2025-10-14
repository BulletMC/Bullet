package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.net.ClientSession
import com.maddoxh.bullet.net.PacketSender.sendPacket
import com.maddoxh.bullet.types.DataPack
import com.maddoxh.bullet.types.KnownPacksUtils.readKnownPacks
import com.maddoxh.bullet.types.KnownPacksUtils.writeKnownPacks
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

class ConfigState(private val client: ClientSession): SessionState {
    private val knownPacks = listOf(
        DataPack("minecraft", "vanilla", "1.21.8")
    )

    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        when(packetId) {
            0x07 -> {
                val packs = input.readKnownPacks()
                Bullet.logger.info("Client reports ${packs.size} known packs: ${packs.joinToString { it.id }}")
            }

            else -> Bullet.logger.warn("Unknown packet ID $packetId in config state")
        }
    }

    suspend fun sendSelectKnownPacks(output: ByteWriteChannel) {
        sendPacket(output, 0x0E) {
            writeKnownPacks(knownPacks)
        }
    }
}
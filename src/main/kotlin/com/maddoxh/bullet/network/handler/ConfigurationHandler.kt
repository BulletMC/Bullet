package com.maddoxh.bullet.network.handler

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.io.MinecraftOutputStream
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.`in`.config.AcknowledgeFinishConfiguration
import com.maddoxh.bullet.network.packet.impl.`in`.config.ClientInformation
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundKnownPacks
import com.maddoxh.bullet.network.packet.impl.`in`.config.ServerboundPluginMessage
import com.maddoxh.bullet.network.packet.impl.out.config.ClientboundKnownPacks
import com.maddoxh.bullet.network.packet.impl.out.config.ClientboundPluginMessage
import com.maddoxh.bullet.network.packet.impl.out.config.FeatureFlags
import com.maddoxh.bullet.network.packet.impl.out.config.FinishConfiguration
import com.maddoxh.bullet.network.packet.impl.out.config.RegistryData
import com.maddoxh.bullet.network.packet.impl.out.config.RegistryEntry
import com.maddoxh.bullet.registry.RegistryManager
import com.maddoxh.bullet.state.ConnectionState
import java.io.ByteArrayOutputStream

class ConfigurationHandler : PacketHandler {
    override fun handle(packet: InboundPacket, connection: ClientConnection) {
        when(packet) {
            is ServerboundPluginMessage -> {
                if(packet.channel == "minecraft:brand") {
                    val brand = String(packet.data.drop(1).toByteArray())
                    logger.info("[*] Client brand: $brand")
                }

                sendServerBrand(connection)
                connection.send(FeatureFlags)
            }

            is ClientInformation -> {
                logger.info("[*] Client information: locale=${packet.locale} viewDist=${packet.viewDistance}")
            }

            is ServerboundKnownPacks -> {
                logger.info("[*] Client knows: ${packet.packs.size} packs")
                connection.send(ClientboundKnownPacks)
                sendAllRegistries(connection)
                connection.send(FinishConfiguration)
                logger.info("[*] Sent FinishConfiguration, waiting for acknowledgement..")
            }

            is AcknowledgeFinishConfiguration -> {
                logger.info("[*] Configuration acknowledged, transitioning to PLAY")
                connection.state = ConnectionState.PLAY
            }

            else -> logger.warn("[!] ConfigurationHandler received unexpected packet: $packet")
        }
    }

    private fun sendServerBrand(connection: ClientConnection) {
        val brand = "BulletMC"
        val brandBytes = brand.toByteArray()

        val buf = ByteArrayOutputStream()
        MinecraftOutputStream(buf).apply {
            writeVarInt(brandBytes.size)
            write(brandBytes)
        }

        connection.send(ClientboundPluginMessage("minecraft:brand", buf.toByteArray()))
        logger.info("[*] Sent server brand: $brand")
    }

    private fun sendAllRegistries(connection: ClientConnection) {
        for(registryID in RegistryManager.REGISTRY_SEND_ORDER) {
            val entries = RegistryManager.getRegistry(registryID)
            if(entries.isEmpty()) {
                logger.warn("[!] Registry $registryID is empty (skipping)")
                continue
            }

            val registryEntries = entries.map { (id, nbt) ->
                RegistryEntry(id, nbt)
            }

            connection.send(RegistryData(registryID, registryEntries))
            logger.info("[*] Sent registry $registryID (${entries.size} entries)")
        }
    }
}
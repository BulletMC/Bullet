package com.aznos.packets.handlers

import com.aznos.ClientSession
import com.aznos.GameState
import com.aznos.packets.PacketHandler
import com.aznos.packets.PacketReceiver
import com.aznos.packets.configuration.`in`.ClientConfigFinishAckPacket
import com.aznos.packets.configuration.`in`.ClientConfigPacksPacket
import com.aznos.packets.configuration.out.ServerConfigFinishPacket
import com.aznos.packets.configuration.out.ServerConfigPacksPacket
import com.aznos.packets.configuration.out.ServerConfigRegistryData
import com.aznos.registry.Registries

class ConfigPacketHandler(client: ClientSession) : PacketHandler(client) {

    init {
        client.sendPacket(ServerConfigPacksPacket())
    }

    @PacketReceiver
    fun handleKnownPacks(packet: ClientConfigPacksPacket) {
        sendRegistries()
        client.sendPacket(ServerConfigFinishPacket())
    }

    @PacketReceiver
    fun handleFinishAck(packet: ClientConfigFinishAckPacket) {
        client.changeNetworkState(GameState.PLAY)
    }

    private fun sendRegistries() {
        client.sendPacket(ServerConfigRegistryData.fromRegistry(Registries.dimension_type))
        client.sendPacket(ServerConfigRegistryData.fromRegistry(Registries.biomes))
        client.sendPacket(ServerConfigRegistryData.fromRegistry(Registries.wolf_variant))
        client.sendPacket(ServerConfigRegistryData.fromRegistry(Registries.damage_type))
        client.sendPacket(ServerConfigRegistryData.fromRegistry(Registries.painting_variant))
    }

}
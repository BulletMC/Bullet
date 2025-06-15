package com.aznos.packets.play.`in`

import com.aznos.ClientSession
import com.aznos.datatypes.StringType.readString
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.events.EventManager
import com.aznos.events.PlayerBrandEvent
import com.aznos.packets.Packet
import net.kyori.adventure.text.Component
import java.io.ByteArrayInputStream
import java.io.DataInputStream

/**
 * Mods and plugins can use this to send their data, minecraft uses some plugin channels
 *
 * @property channel Name of the plugin channel used to send the data
 * @property pluginData Any data
 */
@Suppress("unused")
class ClientPluginMessagePacket(data: ByteArray) : Packet(data) {
    val channel: String
    val pluginData: ByteArray

    init {
        val input = getIStream()

        channel = input.readString()
        pluginData = input.readNBytes(input.available())
    }

    override fun apply(client: ClientSession) {
        when(channel) {
            "minecraft:brand" -> {
                val input = DataInputStream(ByteArrayInputStream(pluginData))
                val length = input.readVarInt()

                val brandBytes = ByteArray(length)
                input.readFully(brandBytes)

                val brand = String(brandBytes, Charsets.UTF_8)
                client.player.brand = brand

                val event = PlayerBrandEvent(client.player, brand)
                EventManager.fire(event)
                if(event.isCancelled) {
                    client.player.disconnect(Component.text("Your client brand is not supported"))
                    return
                }
            }
        }
    }
}
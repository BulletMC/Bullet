package com.aznos.packets.configuration.out

import com.aznos.Bullet
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.newPacket.Keyed
import com.aznos.packets.newPacket.ResourceLocation
import com.aznos.packets.newPacket.ServerPacket

/**
 * This packet is sent to inform the client which datapacks are present on the server.
 */
class ServerConfigPacksPacket : ServerPacket(key) {

    companion object {
        val key = Keyed(0x0E, ResourceLocation.vanilla("config.out.select_known_packs"))
    }

    override fun retrieveData(): ByteArray {
        return writeData {
            writeVarInt(1)
            writeString("minecraft")
            writeString("core")
            writeString(Bullet.VERSION)
        }
    }

}
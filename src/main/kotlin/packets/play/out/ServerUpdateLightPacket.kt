package com.aznos.packets.play.out

import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.data.chunk.LightData
import com.aznos.packets.newPacket.Keyed
import com.aznos.packets.newPacket.ResourceLocation
import com.aznos.packets.newPacket.ServerPacket

class ServerUpdateLightPacket(
    var x: Int,
    var z: Int,
    var lightData: LightData
): ServerPacket(key) {

    companion object {
        val key = Keyed(0x2B, ResourceLocation.vanilla("play.out.light_update"))
    }

    override fun retrieveData(): ByteArray {
        return writeData {
            writeVarInt(x)
            writeVarInt(z)
            LightData.write(this, lightData)
        }
    }

}
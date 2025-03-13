package packets.status.out

import com.aznos.datatypes.StringType.writeString
import com.aznos.packets.data.ServerStatusResponse
import com.aznos.packets.newPacket.Keyed
import com.aznos.packets.newPacket.ResourceLocation
import com.aznos.packets.newPacket.ServerPacket
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

/**
 * Packet for sending the server status response to the client status request packet
 *
 * @param response The response of the server status request
 */
class ServerStatusResponsePacket(val response: ServerStatusResponse) : ServerPacket(key) {

    companion object {
        val key = Keyed(0x00, ResourceLocation.vanilla("status.out.status_response"))
    }

    override fun retrieveData(): ByteArray {
        return writeData {
            val json = JsonObject()

            json.add("version", JsonObject().apply {
                addProperty("name", response.version.name)
                addProperty("protocol", response.version.protocol)
            })

            json.add("players", JsonObject().apply {
                addProperty("max", response.players.max)
                addProperty("online", response.players.online)
                add("sample", JsonArray())
            })

            json.add("description", GsonComponentSerializer.gson().serializeToTree(response.description))

            if (response.favicon != null)
                json.addProperty("favicon", response.favicon)

            json.addProperty("enforcesSecureChat", response.enforcesSecureChat)
            writeString(json.toString())
        }
    }
}

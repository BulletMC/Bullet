package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.VarInt.readVarInt
import com.aznos.entity.player.data.GameMode
import com.aznos.packets.Packet
import com.aznos.packets.play.out.ServerPlayerPositionAndLookPacket
import com.aznos.packets.play.out.ServerRespawnPacket

/**
 * This packet is sent by the client when it respawns after dying or when
 * the client opens the statistics menu
 *
 * Action ID values:
 * 0 - Sent when the client respawns
 * 1- Sent when the client opens the statistics menu
 *
 * @property actionID The action ID of the packet (see above)
 */
class ClientStatusPacket(data: ByteArray) : Packet(data) {
    val actionID: Int = getIStream().readVarInt()

    override fun apply(client: ClientSession) {
        when(actionID) {
            0 -> { // Perform respawn
                client.player.sendPacket(
                    ServerRespawnPacket(
                        Bullet.dimensionCodec!!,
                        "minecraft:overworld",
                        GameMode.SURVIVAL,
                        false,
                        false,
                        true
                    )
                )

                client.player.status.health = 20
                client.player.status.foodLevel = 20
                client.player.status.saturation = 5.0f
                client.player.status.exhaustion = 0f

                client.player.sendPacket(
                    ServerPlayerPositionAndLookPacket(
                        LocationType.Location(8.5, 2.0, 8.5)
                    )
                )
            }

            1 -> { // Request statistics

            }
        }
    }
}
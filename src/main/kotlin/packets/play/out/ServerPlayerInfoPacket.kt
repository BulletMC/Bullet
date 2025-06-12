package com.aznos.packets.play.out

import com.aznos.Bullet.players
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PlayerProperty
import com.aznos.packets.Packet
import java.util.UUID

/**
 * Sent by the server to update the player list (TAB menu)
 *
 * @param action The action type
 * (0: Add, 1: Update Gamemode, 2: Update Ping, 3: Update Display Name, 4: Remove)
 * @param uuid The UUID of the player
 * @param username The username of the player (only for action 0)
 * @param properties The properties of the player (only for action 0)
 * @param gameMode The game mode of the player (only for action 0 and 1)
 * @param ping The ping of the player (only for action 0 and 2)
 */
class ServerPlayerInfoPacket(
    private val action: Int,
    private val uuid: UUID,
    private val username: String? = null,
    private val properties: List<PlayerProperty> = emptyList(),
    private val gameMode: Int = 0,
    private val ping: Int = 0,
) : Packet(0x32) {
    init {
        wrapper.writeVarInt(action)
        wrapper.writeVarInt(1)

        wrapper.writeUUID(uuid)
        when(action) {
            0 -> { //Add player
                wrapper.writeString(username ?: "")
                wrapper.writeVarInt(properties.size)

                for(property in properties) {
                    wrapper.writeString(property.name)
                    wrapper.writeString(property.value)
                    wrapper.writeBoolean(property.isSigned)
                    if(property.isSigned) {
                        wrapper.writeString(property.signature ?: "")
                    }
                }

                wrapper.writeVarInt(gameMode)
                wrapper.writeVarInt(ping)
                wrapper.writeBoolean(false)
            }

            1 -> {  //Update Gamemode
                wrapper.writeVarInt(gameMode)
            }

            2 -> { //Update Ping
                wrapper.writeVarInt(ping)
            }

            4 -> { //Remove player

            }
        }
    }
}
package com.aznos.packets.play.out

import com.aznos.Bullet.players
import com.aznos.datatypes.StringType.writeString
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.Player
import com.aznos.packets.Packet

/**
 * Sent by the server to update the player list (TAB menu)
 *
 * @param action The action type
 * (0: Add, 1: Update Gamemode, 2: Update Ping, 3: Update Display Name, 4: Remove)
 * @param players List of player info entries
 */
class ServerPlayerInfoPacket(
    private val action: Int,
    player: Player
) : Packet(0x32) {
    init {
        wrapper.writeVarInt(action)
        wrapper.writeVarInt(1)

        wrapper.writeUUID(player.uuid)
        when(action) {
            0 -> { //Add player
                wrapper.writeString(player.username)
                wrapper.writeVarInt(player.properties.size)

                for(property in player.properties) {
                    wrapper.writeString(property.name)
                    wrapper.writeString(property.value)
                    wrapper.writeBoolean(property.isSigned)
                    if(property.isSigned) {
                        wrapper.writeString(property.signature ?: "")
                    }
                }

                wrapper.writeVarInt(player.gameMode.id)
                wrapper.writeVarInt(player.ping)
                wrapper.writeBoolean(false)
            }

            1 -> {  //Update Gamemode
                wrapper.writeVarInt(player.gameMode.id)
            }

            2 -> { //Update Ping
                wrapper.writeVarInt(player.ping)
            }

            4 -> { //Remove player

            }
        }
    }
}
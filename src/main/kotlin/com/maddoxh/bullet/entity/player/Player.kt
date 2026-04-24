package com.maddoxh.bullet.entity.player

import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.entity.LivingEntity
import com.maddoxh.bullet.network.chat.ChatComponent
import com.maddoxh.bullet.network.packet.impl.out.config.ConfigDisconnect
import com.maddoxh.bullet.network.packet.impl.out.login.LoginDisconnect
import com.maddoxh.bullet.network.packet.impl.out.play.PlayDisconnect
import com.maddoxh.bullet.state.ConnectionState
import java.util.UUID

class Player(
    entityID: Int,
    uuid: UUID,
    val username: String,
    val connection: ClientConnection
) : LivingEntity(entityID, uuid) {
    val connectionState: ConnectionState
        get() = connection.state

    val isOnline: Boolean
        get() = !connection.isClosed

    fun disconnect(message: String) {
        val reason = ChatComponent.literal(message)

        when(connection.state) {
            ConnectionState.LOGIN -> {
                connection.send(LoginDisconnect(reason))
            }

            ConnectionState.CONFIGURATION -> {
                connection.send(ConfigDisconnect(reason))
            }

            ConnectionState.PLAY -> {
                connection.send(PlayDisconnect(reason))
            }

            else -> {}
        }

        connection.close()
    }

    override fun tick() {
        // fill in later
    }

    override fun onDeath() {
        // fill in later
    }
}
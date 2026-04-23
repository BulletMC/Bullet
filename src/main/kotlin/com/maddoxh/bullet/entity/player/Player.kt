package com.maddoxh.bullet.entity.player

import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.entity.LivingEntity
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

    override fun tick() {
        // fill in later
    }

    override fun onDeath() {
        // fill in later
    }
}
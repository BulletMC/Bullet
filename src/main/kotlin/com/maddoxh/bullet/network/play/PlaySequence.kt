package com.maddoxh.bullet.network.play

import com.maddoxh.bullet.Bullet.Companion.logger
import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.entity.player.GameMode
import com.maddoxh.bullet.network.packet.impl.`in`.InboundPacket
import com.maddoxh.bullet.network.packet.impl.out.play.GameEvent
import com.maddoxh.bullet.network.packet.impl.out.play.LoginPlay
import com.maddoxh.bullet.network.packet.impl.out.play.PlayerAbilities
import com.maddoxh.bullet.network.packet.impl.out.play.SetHeldItem
import com.maddoxh.bullet.network.packet.impl.out.play.SyncPlayerPosition

object PlaySequence {
    fun begin(connection: ClientConnection) {
        val player = connection.player
            ?: error("PlaySequence.begin called with no player on connection")

        logger.info("[*] Beginning play sequence for ${player.username}")

        connection.send(LoginPlay(
            entityID = player.entityID,
            gameMode = GameMode.CREATIVE,
            isFlat = true
        ))

        connection.send(PlayerAbilities(
            allowFlying = true,
            instantBreak = true
        ))

        connection.send(SetHeldItem(0))
        connection.send(SyncPlayerPosition(x = 0.0, y = 64.0, z = 0.0))
        connection.send(GameEvent(event = GameEvent.START_WAITING_FOR_CHUNKS))
        ChunkSender.sendInitialChunks(connection)

        logger.info("[*] Chunks sent to player")
    }
}
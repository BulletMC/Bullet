package com.maddoxh.bullet.network.play

import com.maddoxh.bullet.ClientConnection
import com.maddoxh.bullet.network.packet.impl.out.play.ChunkDataAndUpdateLight
import com.maddoxh.bullet.network.packet.impl.out.play.SetCenterChunk

object ChunkSender {
    fun sendInitialChunks(connection: ClientConnection, viewDistance: Int = 4) {
        val centerX = 0
        val centerZ = 0

        connection.send(SetCenterChunk(centerX, centerZ))
        for(x in (centerX - viewDistance)..(centerX + viewDistance)) {
            for(z in (centerZ - viewDistance)..(centerZ + viewDistance)) {
                connection.send(ChunkDataAndUpdateLight(x, z))
            }
        }
    }
}
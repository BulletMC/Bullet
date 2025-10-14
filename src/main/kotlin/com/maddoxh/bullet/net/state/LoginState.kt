package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.net.ClientSession
import com.maddoxh.bullet.types.StringUtils.readString
import com.maddoxh.bullet.types.UUIDUtils.readUUID
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

class LoginState(private val client: ClientSession): SessionState {
    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        when(packetId) {
            0x00 -> {
                val name = input.readString()
                val uuid = input.readUUID()
                Bullet.logger.info("Player $name with UUID $uuid is logging in")
            }

            else -> error("Unknown packet ID $packetId in login state")
        }
    }
}
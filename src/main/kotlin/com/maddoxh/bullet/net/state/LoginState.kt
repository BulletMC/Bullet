package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.net.ClientSession
import com.maddoxh.bullet.net.PacketSender.sendPacket
import com.maddoxh.bullet.types.GameProfile
import com.maddoxh.bullet.types.GameProfile.writeProfile
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
                client.profile = GameProfile.Profile(uuid, name)
                Bullet.logger.info("Player $name with UUID $uuid is logging in")

                sendPacket(output, 0x02) {
                    writeProfile(GameProfile.Profile(uuid, name))
                }
            }

            0x03 -> {
                Bullet.logger.info("Login successful for ${client.profile?.username}")
            }

            else -> error("Unknown packet ID $packetId in login state")
        }
    }
}
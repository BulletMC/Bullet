package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.net.PacketSender.sendPacket
import com.maddoxh.bullet.types.StringUtils.writeString
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readLong
import io.ktor.utils.io.core.writeLong
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StatusState(private val clientProtocol: Int): SessionState {
    private val statusProvider = {
        StatusResponse(
            Version("BulletMC 1.21.8", clientProtocol),
            Players(100, 0, emptyList()),
            TextComponent("A Minecraft server powered by BulletMC"),
            previewsChat = false,
            enforcesSecureChat = false
        )
    }

    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        when(packetId) {
            0x00 -> {
                val json = Json.encodeToString(statusProvider())
                sendPacket(output, 0x00) {
                    writeString(json)
                }
            }

            0x01 -> {
                val payload = input.readLong()
                sendPacket(output, 0x01) {
                    writeLong(payload)
                }
            }

            else -> error("Unknown packet ID $packetId in status state")
        }
    }
}

@Suppress("MaxLineLength")
@Serializable data class StatusResponse(
    val version: Version,
    val players: Players,
    val description: TextComponent,
    val favicon: String? = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAexJREFUeJztm0GOgjAUht8I0WAmuuFGnsPNLJx4iFlNMlcwmYUbr+cGYqgQcWZhMLS2ULD1J+F9CQmR+vjfZ4GGxLfVavVHIyYkIjoej+gcEOI4pgk6BBoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgYQHoAGhYADoAGhaADoCGBaADoAmrnTRNabFY3PeHSJXPJaHpwOF76fxkfVh/JXTY3rKsd4l0zIUQo4AhUomocCFEEuB76meiICKieTR1Us+FkJfOAFeNm2gSYpLhRUAmCu/N2lAXst4lWgm9HoOZKKRNpWvzuhpNiLwkkZedxhy2S+0l3kvAPJpKWxtNsqp6XYhmAUWzwHhc5KV2jE7CSxZCTbKaxPTFJEfkJf1+vEsS4I9B3/eKtkullwCb614d47NRtcloFhgb3+xP0s3QKKD+zLaZopko6FJeHz4Pg8lDPZtaddp+RRXb5oksZkB6Ot+baEM3TpXSVu9SXq3P1wVd80QWAp4N0/X7Lpvf7E/3/ZcuhFDUGyYa4FLYNX0aVpEEDP19gIuGVYwz4PNHPF3cFVXjXl+I1Iv7ONFQGf07QRaADoCGBaADoGEB6ABoWAA6ABoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgCYlu/6IeK/8hQ6uwCcyPRQAAAABJRU5ErkJggg==",
    val previewsChat: Boolean? = null,
    val enforcesSecureChat: Boolean? = null
)

@Serializable data class Version(val name: String, val protocol: Int)
@Serializable data class Players(val max: Int, val online: Int, val sample: List<PlayerSample> = emptyList())
@Serializable data class PlayerSample(val name: String, val id: String)
@Serializable data class TextComponent(val text: String)
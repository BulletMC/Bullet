package com.maddoxh.bullet.net.state

import com.maddoxh.bullet.Bullet
import com.maddoxh.bullet.net.ClientSession
import com.maddoxh.bullet.packets.handshake.Handshake
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.ByteReadPacket

class HandshakeState(private val session: ClientSession): SessionState {
    override suspend fun handleIncoming(packetId: Int, input: ByteReadPacket, output: ByteWriteChannel) {
        if(packetId != Handshake.PACKET_ID) {
            error("Unexpected packet ID $packetId in HandshakeState")
        }

        val handshake = Handshake.readFrom(input)
        when(handshake.nextState) {
            Handshake.State.STATUS -> session.switchState(StatusState(handshake.protocolVersion))
            Handshake.State.LOGIN -> session.switchState(LoginState(session))
            else -> Bullet.logger.error("Unknown next state ${handshake.nextState} in Handshake packet")
        }
    }
}
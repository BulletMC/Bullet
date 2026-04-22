package com.maddoxh.bullet.network.packet

sealed class Packet {
    sealed class Handshake : Packet() {
        data class Intention( // 0x00 C->S
            val protocolVersion: Int,
            val serverAddress: String,
            val serverPort: Int,
            val nextState: Int
        ) : Handshake()
    }

    sealed class Status : Packet() {
        object StatusRequest : Status() // 0x00 C->S

        data class PingRequest( // 0x01 C->S
            val payload: Long
        ) : Status()

        data class StatusResponse( // 0x00 S->C
            val json: String
        ) : Status()

        data class PongResponse( // 0x01 S->C
            val payload: Long
        ) : Status()
    }
}
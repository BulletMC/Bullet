package com.maddoxh.bullet.types

import com.maddoxh.bullet.types.VarInt.readVarInt
import com.maddoxh.bullet.types.VarInt.writeVarInt
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.*
import java.util.UUID

object UUIDUtils {
    suspend fun ByteReadPacket.readUUID(): UUID {
        val mostSigBits = readLong()
        val leastSigBits = readLong()
        return UUID(mostSigBits, leastSigBits)
    }

    fun BytePacketBuilder.writeUUID(uuid: UUID) {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
    }
}
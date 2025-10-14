package com.maddoxh.bullet.types

import com.maddoxh.bullet.types.StringUtils.readString
import com.maddoxh.bullet.types.StringUtils.writeString
import com.maddoxh.bullet.types.VarInt.readVarInt
import com.maddoxh.bullet.types.VarInt.writeVarInt
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket

data class DataPack(val namespace: String, val id: String, val version: String)

object KnownPacksUtils {
    fun BytePacketBuilder.writeKnownPacks(packs: List<DataPack>) {
        writeVarInt(packs.size)
        for(pack in packs) {
            writeString(pack.namespace)
            writeString(pack.id)
            writeString(pack.version)
        }
    }

    suspend fun ByteReadPacket.readKnownPacks(): List<DataPack> {
        val count = readVarInt()
        val out = ArrayList<DataPack>(count)
        repeat(count) {
            val ns = readString()
            val id = readString()
            val ver = readString()
            out.add(DataPack(ns, id, ver))
        }

        return out
    }
}
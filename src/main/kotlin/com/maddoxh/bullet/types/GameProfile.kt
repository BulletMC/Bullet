package com.maddoxh.bullet.types

import com.maddoxh.bullet.types.StringUtils.readString
import com.maddoxh.bullet.types.StringUtils.writeString
import com.maddoxh.bullet.types.UUIDUtils.readUUID
import com.maddoxh.bullet.types.UUIDUtils.writeUUID
import com.maddoxh.bullet.types.VarInt.readVarInt
import com.maddoxh.bullet.types.VarInt.writeVarInt
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import java.util.UUID

object GameProfile {
    data class Profile(
        val uuid: UUID,
        val username: String,
        val properties: List<Property> = emptyList()
    )

    data class Property(
        val name: String,
        val value: String,
        val signature: String? = null
    )

    suspend fun ByteReadPacket.readProfile(): Profile {
        val uuid = readUUID()
        val username = readString()
        val count = readVarInt()
        val props = ArrayList<Property>(count)
        repeat(count) {
            val name = readString()
            val value = readString()
            val hasSignature = readBoolean()
            val signature = if(hasSignature) readString() else null
            props.add(Property(name, value, signature))
        }

        return Profile(uuid, username, props)
    }

    fun BytePacketBuilder.writeProfile(profile: Profile) {
        writeUUID(profile.uuid)
        writeString(profile.username)
        writeVarInt(profile.properties.size)
        for(p in profile.properties) {
            writeString(p.name)
            writeString(p.value)
            writeBoolean(p.signature != null)
            if(p.signature != null) writeString(p.signature)
        }
    }

    private fun ByteReadPacket.readBoolean(): Boolean = (readByte().toInt() and 0xFF) != 0
    private fun BytePacketBuilder.writeBoolean(v: Boolean) = writeByte(if(v) 1 else 0)
}
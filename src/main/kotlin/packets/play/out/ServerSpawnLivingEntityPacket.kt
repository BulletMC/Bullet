package com.aznos.packets.play.out

import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.entity.player.data.Location
import com.aznos.packets.Packet
import java.util.UUID

/**
 * Used to spawn a living entity
 * If spawning a player you should instead see [ServerSpawnPlayerPacket]
 *
 * @param entityID The new entityID, this should be unique
 * @param entityUUID The new UUID of the entity
 * @param type The type of entity to spawn in
 * @param location The location of the entity
 * @param headPitch The head pitch of the entity
 * @param velocityX The velocity of the entity on the X axis
 * @param velocityY The velocity of the entity on the Y axis
 * @param velocityZ The velocity of the entity on the Z axis
 */
class ServerSpawnLivingEntityPacket(
    entityID: Int,
    entityUUID: UUID,
    type: Int,
    location: Location,
    headPitch: Float,
    velocityX: Short,
    velocityY: Short,
    velocityZ: Short
) : Packet(0x02) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeUUID(entityUUID)
        wrapper.writeVarInt(type)

        wrapper.writeDouble(location.x)
        wrapper.writeDouble(location.y)
        wrapper.writeDouble(location.z)
        wrapper.writeByte((location.yaw * 256.0f / 360.0f).toInt())
        wrapper.writeByte((location.pitch * 256.0f / 360.0f).toInt())
        wrapper.writeByte((headPitch * 256.0f / 360.0f).toInt())

        wrapper.writeShort(velocityX.toInt())
        wrapper.writeShort(velocityY.toInt())
        wrapper.writeShort(velocityZ.toInt())
    }
}
package com.aznos.packets.play.out

import com.aznos.datatypes.LocationType
import com.aznos.datatypes.LocationType.writeLocationAngle
import com.aznos.datatypes.UUIDType.writeUUID
import com.aznos.datatypes.VarInt.writeVarInt
import com.aznos.packets.Packet
import java.util.UUID

/**
 * Used to spawn a living entity
 * If spawning a player you should instead see [ServerSpawnPlayerPacket]
 * and if spawning a non-living entity, see [ServerSpawnEntityPacket]
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
    location: LocationType.Location,
    headPitch: Float,
    velocityX: Short,
    velocityY: Short,
    velocityZ: Short
) : Packet(0x02) {
    init {
        wrapper.writeVarInt(entityID)
        wrapper.writeUUID(entityUUID)
        wrapper.writeVarInt(type)

        wrapper.writeLocationAngle(location)
        wrapper.writeByte((headPitch * 256.0f / 360.0f).toInt())

        wrapper.writeShort(velocityX.toInt())
        wrapper.writeShort(velocityY.toInt())
        wrapper.writeShort(velocityZ.toInt())
    }
}
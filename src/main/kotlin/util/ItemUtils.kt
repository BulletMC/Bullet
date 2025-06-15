package com.aznos.util

import com.aznos.Bullet.players
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.MetadataType
import com.aznos.entity.DroppedItem
import com.aznos.packets.play.out.ServerEntityMetadataPacket
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import com.aznos.world.World
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack

object ItemUtils {
    fun dropItem(
        world: World,
        blockPos: BlockPositionType.BlockPosition,
        item: Int,
        vx: Short = 0, vy: Short = 0, vz: Short = 0
    ) {
        val drop = ItemStack.of(Item.getItemFromID(item) ?: Item.AIR)
        val itemEntity = DroppedItem()

        val loc = LocationType.Location(blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, 0f, 0f)
        itemEntity.location = loc

        world.items.add(Pair(itemEntity, drop))

        for (player in players) {
            player.sendPacket(
                ServerSpawnEntityPacket(
                    itemEntity.entityID, itemEntity.uuid,
                    37,
                    loc,
                    vx, vy, vz,
                    1
                )
            )

            player.sendPacket(
                ServerEntityMetadataPacket(
                    itemEntity.entityID,
                    listOf(MetadataType.MetadataEntry(7, 6, drop.toSlotData()))
                )
            )
        }
    }
}
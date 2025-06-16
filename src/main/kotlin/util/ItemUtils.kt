package com.aznos.util

import com.aznos.Bullet.players
import com.aznos.ClientSession
import com.aznos.datatypes.BlockPositionType
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.MetadataType
import com.aznos.entity.DroppedItem
import com.aznos.entity.Entity
import com.aznos.packets.play.out.ServerCollectItemPacket
import com.aznos.packets.play.out.ServerDestroyEntitiesPacket
import com.aznos.packets.play.out.ServerEntityMetadataPacket
import com.aznos.packets.play.out.ServerSoundEffectPacket
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import com.aznos.world.World
import com.aznos.world.blocks.BlockTags
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds
import kotlin.collections.removeAll
import kotlin.math.pow
import kotlin.math.sqrt

object ItemUtils {
    /**
     * Drops an item in the world at the specified position with the given velocity
     *
     * @param world The world to drop the item in
     * @param blockPos The position to drop the item at
     * @param item The item ID to drop
     * @param vx The velocity in the x direction (default is 0)
     * @param vy The velocity in the y direction (default is 0)
     * @param vz The velocity in the z direction (default is 0)
     */
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

        for(player in players) {
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

    /**
     * Checks if the player is close enough to pick up items in the world
     *
     * @param client The client session of the player
     * @param world The world to check for items
     */
    fun checkItems(client: ClientSession, world: World) {
        val now = System.currentTimeMillis()
        val player = client.player
        val picked = mutableListOf<Pair<Entity, ItemStack>>()

        for(item in world.items) {
            if(now - item.first.spawnTimeMs < item.first.pickupDelayMs) continue

            val distance = sqrt(
                (player.location.x - item.first.location.x).pow(2) +
                        (player.location.y - item.first.location.y).pow(2) +
                        (player.location.z - item.first.location.z).pow(2)
            )

            if(distance <= 1.25) {
                player.sendPacket(
                    ServerCollectItemPacket(
                        item.first.entityID,
                        player.entityID,
                        1
                    )
                )

                player.sendPacket(ServerDestroyEntitiesPacket(intArrayOf(item.first.entityID)))
                player.sendPacket(
                    ServerSoundEffectPacket(
                        Sounds.ENTITY_ITEM_PICKUP,
                        SoundCategories.PLAYER,
                        player.location.x.toInt(),
                        player.location.y.toInt(),
                        player.location.z.toInt()
                    )
                )

                player.addItem(item.second)
                picked += item
            }
        }

        world.items.removeAll(picked)
    }

    /**
     * Returns the maximum durability of an item based on its type
     * 
     * @param itemStack The item stack to check
     * @return The maximum durability of the item, or 0 if it is not a tool
     */
    fun getMaxItemDurability(itemStack: ItemStack): Int {
        for(woodTool in BlockTags.WOODEN_TOOLS) {
            if(itemStack.item.id == woodTool.id) return 59
        }

        for(stoneTool in BlockTags.STONE_TOOLS) {
            if(itemStack.item.id == stoneTool.id) return 131
        }

        for(ironTool in BlockTags.IRON_TOOLS) {
            if(itemStack.item.id == ironTool.id) return 250
        }

        for(goldTool in BlockTags.GOLDEN_TOOLS) {
            if(itemStack.item.id == goldTool.id) return 32
        }

        for(diamondTool in BlockTags.DIAMOND_TOOLS) {
            if(itemStack.item.id == diamondTool.id) return 1561
        }

        for(netheriteTool in BlockTags.NETHERITE_TOOLS) {
            if(itemStack.item.id == netheriteTool.id) return 2031
        }

        return 0
    }
}
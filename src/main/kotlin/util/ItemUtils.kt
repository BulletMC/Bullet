package com.aznos.util

import com.aznos.Bullet
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
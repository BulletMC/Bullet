package com.aznos.entity.livingentity

import com.aznos.Bullet
import com.aznos.entity.Entity
import com.aznos.world.World
import com.aznos.world.items.ItemStack
import java.util.UUID

/**
 * Represents a Non-Player Character (NPC) entity in the game.
 *
 * NPCs are entities that can interact with players, hold items, and have specific behaviors.
 *
 * @property world The world in which the NPC exists.
 * @property heldItem The item currently held by the NPC, if any.
 * @property isSneaking Indicates whether the NPC is currently sneaking.
 */
class NPCEntity : Entity() {
    override lateinit var uuid: UUID
    var world: World? = Bullet.storage.getWorlds()[0]
    var heldItem: ItemStack? = null
    var isSneaking: Boolean = false
}
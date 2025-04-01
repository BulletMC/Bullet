package com.aznos.world.blocks

import com.aznos.world.items.Item

object BlockTags {
    val SIGNS = listOf(
        Item.OAK_SIGN,
        Item.SPRUCE_SIGN,
        Item.BIRCH_SIGN,
        Item.JUNGLE_SIGN,
        Item.ACACIA_SIGN,
        Item.DARK_OAK_SIGN,
        Item.CRIMSON_SIGN,
        Item.WARPED_SIGN
    )

    val HOSTILE_SPAWN_EGGS = listOf(
        Item.BEE_SPAWN_EGG,
        Item.BLAZE_SPAWN_EGG,
        Item.CAVE_SPIDER_SPAWN_EGG,
        Item.CREEPER_SPAWN_EGG,
        Item.DOLPHIN_SPAWN_EGG,
        Item.DROWNED_SPAWN_EGG,
        Item.ELDER_GUARDIAN_SPAWN_EGG,
        Item.ENDERMAN_SPAWN_EGG,
        Item.EVOKER_SPAWN_EGG,
        Item.GHAST_SPAWN_EGG,
        Item.GUARDIAN_SPAWN_EGG,
        Item.HOGLIN_SPAWN_EGG,
        Item.HUSK_SPAWN_EGG,
        Item.LLAMA_SPAWN_EGG,
        Item.MAGMA_CUBE_SPAWN_EGG,
        Item.PANDA_SPAWN_EGG,
        Item.PHANTOM_SPAWN_EGG,
        Item.PIGLIN_SPAWN_EGG,
        Item.PIGLIN_BRUTE_SPAWN_EGG,
        Item.PILLAGER_SPAWN_EGG,
        Item.POLAR_BEAR_SPAWN_EGG,
        Item.RAVAGER_SPAWN_EGG,
        Item.SHULKER_SPAWN_EGG,
        Item.SILVERFISH_SPAWN_EGG,
        Item.SKELETON_SPAWN_EGG,
        Item.SLIME_SPAWN_EGG,
        Item.SPIDER_SPAWN_EGG,
        Item.STRAY_SPAWN_EGG,
        Item.TRADER_LLAMA_SPAWN_EGG,
        Item.VEX_SPAWN_EGG,
        Item.VINDICATOR_SPAWN_EGG,
        Item.WITCH_SPAWN_EGG,
        Item.WITHER_SKELETON_SPAWN_EGG,
        Item.WOLF_SPAWN_EGG,
        Item.ZOGLIN_SPAWN_EGG,
        Item.ZOMBIE_SPAWN_EGG,
        Item.ZOMBIE_VILLAGER_SPAWN_EGG,
        Item.ZOMBIFIED_PIGLIN_SPAWN_EGG,
    )

    val PASSIVE_SPAWN_EGGS = listOf(
        Item.BAT_SPAWN_EGG,
        Item.CAT_SPAWN_EGG,
        Item.CHICKEN_SPAWN_EGG,
        Item.COD_SPAWN_EGG,
        Item.COW_SPAWN_EGG,
        Item.DONKEY_SPAWN_EGG,
        Item.FOX_SPAWN_EGG,
        Item.HORSE_SPAWN_EGG,
        Item.MOOSHROOM_SPAWN_EGG,
        Item.MULE_SPAWN_EGG,
        Item.OCELOT_SPAWN_EGG,
        Item.PARROT_SPAWN_EGG,
        Item.PIG_SPAWN_EGG,
        Item.PUFFERFISH_SPAWN_EGG,
        Item.RABBIT_SPAWN_EGG,
        Item.SALMON_SPAWN_EGG,
        Item.SHEEP_SPAWN_EGG,
        Item.SKELETON_HORSE_SPAWN_EGG,
        Item.SQUID_SPAWN_EGG,
        Item.STRIDER_SPAWN_EGG,
        Item.TROPICAL_FISH_SPAWN_EGG,
        Item.TURTLE_SPAWN_EGG,
        Item.VILLAGER_SPAWN_EGG,
        Item.WANDERING_TRADER_SPAWN_EGG,
        Item.ZOMBIE_HORSE_SPAWN_EGG
    )

    val SPAWN_EGGS = HOSTILE_SPAWN_EGGS + PASSIVE_SPAWN_EGGS
}
package com.aznos.entity.nonliving

/**
 * Represents all non-living entities that can be spawned in
 *
 * @param id The ID of the entity
 */
enum class Entities(val id: Int) {
    AREA_EFFECT_CLOUD(0),
    ARMOR_STAND(1),
    ARROW(2),
    BOAT(6),
    DRAGON_FIREBALL(15),
    END_CRYSTAL(18),
    EVOKER(22),
    EYE_OF_ENDER(24),
    FALLING_BLOCK(26),
    FIREWORK_ROCKET_ENTITY(27),
    IRON_GOLEM(36),
    ITEM_FRAME(38),
    FIREBALL(39),
    LEASH_KNOT(40),
    LIGHTNING_BOLT(41),
    LLAMA_SPIT(43),
    MINECART(45),
    MINECART_CHEST(46),
    MINECART_COMMAND_BLOCK(47),
    MINECART_FURNACE(48),
    MINECART_HOPPER(49),
    MINECART_MOB_SPAWNER(50),
    MINECART_TNT(51),
    SHULKER_BULLET(71),
    SMALL_FIREBALL(76),
    SNOWBALL(78),
    SPECTRAL_ARROW(79),
    THROWN_EGG(84),
    THROWN_ENDER_PEARL(85),
    THROWN_EXP_BOTTLE(86),
    THROWN_POTION(87),
    THROWN_TRIDENT(88),
    WITHER_SKULL(99),
    FISHING_HOOK(107)
}
package com.aznos.world

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

/**
 * A block in the world.
 */
enum class Block(val id: Int, val minecraftID: String) {
    AIR(0, "minecraft:air"),
    STONE(1, "minecraft:stone"),
    GRANITE(2, "minecraft:granite"),
    POLISHED_GRANITE(3, "minecraft:polished_granite"),
    DIORITE(4, "minecraft:diorite"),
    POLISHED_DIORITE(5, "minecraft:polished_diorite"),
    ANDESITE(6, "minecraft:andesite"),
    POLISHED_ANDESITE(7, "minecraft:polished_andesite"),

    GRASS_BLOCK(8, "minecraft:grass_block"),
    DIRT(9, "minecraft:dirt"),
    COARSE_DIRT(10, "minecraft:coarse_dirt"),
    PODZOL(11, "minecraft:podzol"),

    COBBLESTONE(12, "minecraft:cobblestone"),
    PLANKS(13, "minecraft:planks"),
    SAPLING(14, "minecraft:sapling"),
    BEDROCK(15, "minecraft:bedrock"),

    WATER_FLOWING(16, "minecraft:flowing_water"),
    WATER(17, "minecraft:water"),
    LAVA_FLOWING(18, "minecraft:flowing_lava"),
    LAVA(19, "minecraft:lava"),

    SAND(20, "minecraft:sand"),
    RED_SAND(21, "minecraft:red_sand"),
    GRAVEL(22, "minecraft:gravel"),

    GOLD_ORE(23, "minecraft:gold_ore"),
    IRON_ORE(24, "minecraft:iron_ore"),
    COAL_ORE(25, "minecraft:coal_ore"),

    OAK_LOG(26, "minecraft:log"),
    LEAVES(27, "minecraft:leaves"),
    SPONGE(28, "minecraft:sponge"),
    GLASS(29, "minecraft:glass"),

    LAPIS_ORE(30, "minecraft:lapis_ore"),
    LAPIS_BLOCK(31, "minecraft:lapis_block"),
    DISPENSER(32, "minecraft:dispenser"),

    SANDSTONE(33, "minecraft:sandstone"),
    CHISELED_SANDSTONE(34, "minecraft:chiseled_sandstone"),
    SMOOTH_SANDSTONE(35, "minecraft:smooth_sandstone"),

    NOTE_BLOCK(36, "minecraft:noteblock"),
    BED(37, "minecraft:bed"),
    POWERED_RAIL(38, "minecraft:golden_rail"),
    DETECTOR_RAIL(39, "minecraft:detector_rail"),
    STICKY_PISTON(40, "minecraft:sticky_piston"),

    COBWEB(41, "minecraft:web"),
    TALL_GRASS(42, "minecraft:tallgrass"),
    DEAD_BUSH(43, "minecraft:deadbush"),
    PISTON(44, "minecraft:piston"),
    PISTON_HEAD(45, "minecraft:piston_head"),

    WOOL(46, "minecraft:wool"),
    DANDELION(47, "minecraft:yellow_flower"),
    POPPY(48, "minecraft:red_flower"),
    BROWN_MUSHROOM(49, "minecraft:brown_mushroom"),
    RED_MUSHROOM(50, "minecraft:red_mushroom"),

    GOLD_BLOCK(51, "minecraft:gold_block"),
    IRON_BLOCK(52, "minecraft:iron_block"),
    DOUBLE_STONE_SLAB(53, "minecraft:double_stone_slab"),
    STONE_SLAB(54, "minecraft:stone_slab"),
    BRICK_BLOCK(55, "minecraft:brick_block"),
    TNT(56, "minecraft:tnt"),
    BOOKSHELF(57, "minecraft:bookshelf"),

    MOSSY_COBBLESTONE(58, "minecraft:mossy_cobblestone"),
    OBSIDIAN(59, "minecraft:obsidian"),
    TORCH(60, "minecraft:torch"),
    FIRE(61, "minecraft:fire"),

    MOB_SPAWNER(62, "minecraft:mob_spawner"),
    OAK_STAIRS(63, "minecraft:oak_stairs"),
    CHEST(64, "minecraft:chest"),
    REDSTONE_WIRE(65, "minecraft:redstone_wire"),
    DIAMOND_ORE(66, "minecraft:diamond_ore"),
    DIAMOND_BLOCK(67, "minecraft:diamond_block"),

    CRAFTING_TABLE(68, "minecraft:crafting_table"),
    WHEAT(69, "minecraft:wheat"),
    FARMLAND(70, "minecraft:farmland"),
    FURNACE(71, "minecraft:furnace"),
    LIT_FURNACE(72, "minecraft:lit_furnace"),

    STANDING_SIGN(73, "minecraft:standing_sign"),
    WOODEN_DOOR(74, "minecraft:wooden_door"),
    LADDER(75, "minecraft:ladder"),
    RAIL(76, "minecraft:rail"),
    STONE_STAIRS(77, "minecraft:stone_stairs"),

    WALL_SIGN(78, "minecraft:wall_sign"),
    LEVER(79, "minecraft:lever"),
    STONE_PRESSURE_PLATE(80, "minecraft:stone_pressure_plate"),
    IRON_DOOR(81, "minecraft:iron_door"),
    WOODEN_PRESSURE_PLATE(82, "minecraft:wooden_pressure_plate"),

    REDSTONE_ORE(83, "minecraft:redstone_ore"),
    LIT_REDSTONE_ORE(84, "minecraft:lit_redstone_ore"),
    UNLIT_REDSTONE_TORCH(85, "minecraft:unlit_redstone_torch"),
    REDSTONE_TORCH(86, "minecraft:redstone_torch"),

    STONE_BUTTON(87, "minecraft:stone_button"),
    SNOW_LAYER(88, "minecraft:snow_layer"),
    ICE(89, "minecraft:ice"),
    SNOW_BLOCK(90, "minecraft:snow"),

    CACTUS(91, "minecraft:cactus"),
    CLAY(92, "minecraft:clay"),
    SUGAR_CANE(93, "minecraft:reeds"),
    JUKEBOX(94, "minecraft:jukebox"),
    FENCE(95, "minecraft:fence"),

    PUMPKIN(96, "minecraft:pumpkin"),
    NETHERRACK(97, "minecraft:netherrack"),
    SOUL_SAND(98, "minecraft:soul_sand"),
    GLOWSTONE(99, "minecraft:glowstone"),
    NETHER_PORTAL(100, "minecraft:portal"),

    JACK_O_LANTERN(101, "minecraft:lit_pumpkin"),
    CAKE(102, "minecraft:cake"),
    UNPOWERED_REPEATER(103, "minecraft:unpowered_repeater"),
    POWERED_REPEATER(104, "minecraft:powered_repeater"),

    STAINED_GLASS(105, "minecraft:stained_glass"),
    TRAPDOOR(106, "minecraft:trapdoor"),
    STONE_BRICKS(107, "minecraft:stonebrick"),
    MYCELIUM(108, "minecraft:mycelium"),

    WATER_LILY(109, "minecraft:waterlily"),
    NETHER_BRICKS(110, "minecraft:nether_brick"),
    NETHER_WART(111, "minecraft:nether_wart"),
    ENCHANTING_TABLE(112, "minecraft:enchanting_table"),

    QUARTZ_BLOCK(113, "minecraft:quartz_block"),
    QUARTZ_STAIRS(114, "minecraft:quartz_stairs"),
    HOPPER(115, "minecraft:hopper"),
    DROPPER(116, "minecraft:dropper"),

    PRISMARINE(117, "minecraft:prismarine"),
    SEA_LANTERN(118, "minecraft:sea_lantern"),
    HAY_BLOCK(119, "minecraft:hay_block"),

    CARPET(120, "minecraft:carpet"),
    HARDENED_CLAY(121, "minecraft:hardened_clay"),
    COAL_BLOCK(122, "minecraft:coal_block"),
    PACKED_ICE(123, "minecraft:packed_ice"),
    STRUCTURE_BLOCK(124, "minecraft:structure_block");

    companion object {
        private val blocksByID = entries.associateBy { it.id }
        private val blocksByName = entries.associateBy { it.minecraftID }

        fun getBlockByID(id: Int): Block? = blocksByID[id]
        fun getBlockByName(name: String): Block? = blocksByName[name]
    }
}
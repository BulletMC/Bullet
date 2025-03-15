package com.aznos.world

/**
 * Enum of all the blocks in the game
 *
 * @param id The numerical ID of the block
 * @param minecraftID The string ID of the block, (e.g. "minecraft:stone")
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

    SNOWY_GRASS_BLOCK(8, "minecraft:snowy_grass_block"),
    GRASS_BLOCK(9, "minecraft:grass_block"),

    DIRT(10, "minecraft:dirt"),
    COARSE_DIRT(11, "minecraft:coarse_dirt"),
    PODZOL(12, "minecraft:podzol"),

    COBBLESTONE(13, "minecraft:cobblestone"),
    PLANKS(14, "minecraft:planks"),
    SAPLING(15, "minecraft:sapling"),
    BEDROCK(16, "minecraft:bedrock"),

    WATER_FLOWING(17, "minecraft:flowing_water"),
    WATER(18, "minecraft:water"),
    LAVA_FLOWING(19, "minecraft:flowing_lava"),
    LAVA(20, "minecraft:lava"),

    SAND(21, "minecraft:sand"),
    RED_SAND(22, "minecraft:red_sand"),
    GRAVEL(23, "minecraft:gravel"),

    GOLD_ORE(24, "minecraft:gold_ore"),
    IRON_ORE(25, "minecraft:iron_ore"),
    COAL_ORE(26, "minecraft:coal_ore"),

    OAK_LOG(27, "minecraft:log"),
    LEAVES(28, "minecraft:leaves"),
    SPONGE(29, "minecraft:sponge"),
    GLASS(30, "minecraft:glass"),

    LAPIS_ORE(31, "minecraft:lapis_ore"),
    LAPIS_BLOCK(32, "minecraft:lapis_block"),
    DISPENSER(33, "minecraft:dispenser"),

    SANDSTONE(34, "minecraft:sandstone"),
    CHISELED_SANDSTONE(35, "minecraft:chiseled_sandstone"),
    SMOOTH_SANDSTONE(36, "minecraft:smooth_sandstone"),

    NOTE_BLOCK(37, "minecraft:noteblock"),
    BED(38, "minecraft:bed"),
    POWERED_RAIL(39, "minecraft:golden_rail"),
    DETECTOR_RAIL(40, "minecraft:detector_rail"),
    STICKY_PISTON(41, "minecraft:sticky_piston"),

    COBWEB(42, "minecraft:web"),
    TALL_GRASS(43, "minecraft:tallgrass"),
    DEAD_BUSH(44, "minecraft:deadbush"),
    PISTON(45, "minecraft:piston"),
    PISTON_HEAD(46, "minecraft:piston_head"),

    WOOL(47, "minecraft:wool"),
    DANDELION(48, "minecraft:yellow_flower"),
    POPPY(49, "minecraft:red_flower"),
    BROWN_MUSHROOM(50, "minecraft:brown_mushroom"),
    RED_MUSHROOM(51, "minecraft:red_mushroom"),

    GOLD_BLOCK(52, "minecraft:gold_block"),
    IRON_BLOCK(53, "minecraft:iron_block"),
    DOUBLE_STONE_SLAB(54, "minecraft:double_stone_slab"),
    STONE_SLAB(55, "minecraft:stone_slab"),
    BRICK_BLOCK(56, "minecraft:brick_block"),
    TNT(57, "minecraft:tnt"),
    BOOKSHELF(58, "minecraft:bookshelf"),

    MOSSY_COBBLESTONE(59, "minecraft:mossy_cobblestone"),
    OBSIDIAN(60, "minecraft:obsidian"),
    TORCH(61, "minecraft:torch"),
    FIRE(62, "minecraft:fire"),

    MOB_SPAWNER(63, "minecraft:mob_spawner"),
    OAK_STAIRS(64, "minecraft:oak_stairs"),
    CHEST(65, "minecraft:chest"),
    REDSTONE_WIRE(66, "minecraft:redstone_wire"),
    DIAMOND_ORE(67, "minecraft:diamond_ore"),
    DIAMOND_BLOCK(68, "minecraft:diamond_block"),

    CRAFTING_TABLE(69, "minecraft:crafting_table"),
    WHEAT(70, "minecraft:wheat"),
    FARMLAND(71, "minecraft:farmland"),
    FURNACE(72, "minecraft:furnace"),
    LIT_FURNACE(73, "minecraft:lit_furnace"),

    STANDING_SIGN(74, "minecraft:standing_sign"),
    WOODEN_DOOR(75, "minecraft:wooden_door"),
    LADDER(76, "minecraft:ladder"),
    RAIL(77, "minecraft:rail"),
    STONE_STAIRS(78, "minecraft:stone_stairs"),

    WALL_SIGN(79, "minecraft:wall_sign"),
    LEVER(80, "minecraft:lever"),
    STONE_PRESSURE_PLATE(81, "minecraft:stone_pressure_plate"),
    IRON_DOOR(82, "minecraft:iron_door"),
    WOODEN_PRESSURE_PLATE(83, "minecraft:wooden_pressure_plate"),

    REDSTONE_ORE(84, "minecraft:redstone_ore"),
    LIT_REDSTONE_ORE(85, "minecraft:lit_redstone_ore"),
    UNLIT_REDSTONE_TORCH(86, "minecraft:unlit_redstone_torch"),
    REDSTONE_TORCH(87, "minecraft:redstone_torch"),

    STONE_BUTTON(88, "minecraft:stone_button"),
    SNOW_LAYER(89, "minecraft:snow_layer"),
    ICE(90, "minecraft:ice"),
    SNOW_BLOCK(91, "minecraft:snow"),

    CACTUS(92, "minecraft:cactus"),
    CLAY(93, "minecraft:clay"),
    SUGAR_CANE(94, "minecraft:reeds"),
    JUKEBOX(95, "minecraft:jukebox"),
    FENCE(96, "minecraft:fence"),

    PUMPKIN(97, "minecraft:pumpkin"),
    NETHERRACK(98, "minecraft:netherrack"),
    SOUL_SAND(99, "minecraft:soul_sand"),
    GLOWSTONE(100, "minecraft:glowstone"),
    NETHER_PORTAL(101, "minecraft:portal"),

    JACK_O_LANTERN(102, "minecraft:lit_pumpkin"),
    CAKE(103, "minecraft:cake"),
    UNPOWERED_REPEATER(104, "minecraft:unpowered_repeater"),
    POWERED_REPEATER(105, "minecraft:powered_repeater"),

    STAINED_GLASS(106, "minecraft:stained_glass"),
    TRAPDOOR(107, "minecraft:trapdoor"),
    STONE_BRICKS(108, "minecraft:stonebrick"),
    MYCELIUM(109, "minecraft:mycelium"),

    WATER_LILY(110, "minecraft:waterlily"),
    NETHER_BRICKS(111, "minecraft:nether_brick"),
    NETHER_WART(112, "minecraft:nether_wart"),
    ENCHANTING_TABLE(113, "minecraft:enchanting_table"),

    QUARTZ_BLOCK(114, "minecraft:quartz_block"),
    QUARTZ_STAIRS(115, "minecraft:quartz_stairs"),
    HOPPER(116, "minecraft:hopper"),
    DROPPER(117, "minecraft:dropper"),

    PRISMARINE(118, "minecraft:prismarine"),
    SEA_LANTERN(119, "minecraft:sea_lantern"),
    HAY_BLOCK(120, "minecraft:hay_block"),

    CARPET(121, "minecraft:carpet"),
    HARDENED_CLAY(122, "minecraft:hardened_clay"),
    COAL_BLOCK(123, "minecraft:coal_block"),
    PACKED_ICE(124, "minecraft:packed_ice"),
    STRUCTURE_BLOCK(125, "minecraft:structure_block");

    companion object {
        private val blocksByID = values().associateBy { it.id }
        private val blocksByName = values().associateBy { it.minecraftID }

        /**
         * Get a block by its ID
         *
         * @param id The numerical ID of the block
         * @return The block with the given ID, or null if it doesn't exist
         */
        fun getBlockByID(id: Int): Block? = blocksByID[id]

        /**
         * Get a block by its name
         *
         * @param name The string ID of the block
         * @return The block with the given name, or null if it doesn't exist
         */
        fun getBlockByName(name: String): Block? = blocksByName[name]
    }
}
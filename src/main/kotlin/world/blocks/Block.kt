package com.aznos.world.blocks

/**
 * Enum class that contains all the blocks in minecraft
 *
 * @param id The numerical ID of the block
 * @param minecraftID The minecraft string ID of the block (e.g "minecraft:stone")
 * @param defaultState The default state of the block
 */
enum class Block(val id: Int, val minecraftID: String, val defaultState: Int) {
AIR(0, "minecraft:air", 0),

STONE(1, "minecraft:stone", 1),

GRANITE(2, "minecraft:granite", 2),

POLISHED_GRANITE(3, "minecraft:polished_granite", 3),

DIORITE(4, "minecraft:diorite", 4),

POLISHED_DIORITE(5, "minecraft:polished_diorite", 5),

ANDESITE(6, "minecraft:andesite", 6),

POLISHED_ANDESITE(7, "minecraft:polished_andesite", 7),

GRASS_BLOCK(8, "minecraft:grass_block", 9),

DIRT(9, "minecraft:dirt", 10),

COARSE_DIRT(10, "minecraft:coarse_dirt", 11),

PODZOL(11, "minecraft:podzol", 13),

COBBLESTONE(12, "minecraft:cobblestone", 14),

OAK_PLANKS(13, "minecraft:oak_planks", 15),

SPRUCE_PLANKS(14, "minecraft:spruce_planks", 16),

BIRCH_PLANKS(15, "minecraft:birch_planks", 17),

JUNGLE_PLANKS(16, "minecraft:jungle_planks", 18),

ACACIA_PLANKS(17, "minecraft:acacia_planks", 19),

DARK_OAK_PLANKS(18, "minecraft:dark_oak_planks", 20),

OAK_SAPLING(19, "minecraft:oak_sapling", 21),

SPRUCE_SAPLING(20, "minecraft:spruce_sapling", 23),

BIRCH_SAPLING(21, "minecraft:birch_sapling", 25),

JUNGLE_SAPLING(22, "minecraft:jungle_sapling", 27),

ACACIA_SAPLING(23, "minecraft:acacia_sapling", 29),

DARK_OAK_SAPLING(24, "minecraft:dark_oak_sapling", 31),

BEDROCK(25, "minecraft:bedrock", 33),

WATER(26, "minecraft:water", 34),

LAVA(27, "minecraft:lava", 50),

SAND(28, "minecraft:sand", 66),

RED_SAND(29, "minecraft:red_sand", 67),

GRAVEL(30, "minecraft:gravel", 68),

GOLD_ORE(31, "minecraft:gold_ore", 69),

IRON_ORE(32, "minecraft:iron_ore", 70),

COAL_ORE(33, "minecraft:coal_ore", 71),

NETHER_GOLD_ORE(34, "minecraft:nether_gold_ore", 72),

OAK_LOG(35, "minecraft:oak_log", 74),

SPRUCE_LOG(36, "minecraft:spruce_log", 77),

BIRCH_LOG(37, "minecraft:birch_log", 80),

JUNGLE_LOG(38, "minecraft:jungle_log", 83),

ACACIA_LOG(39, "minecraft:acacia_log", 86),

DARK_OAK_LOG(40, "minecraft:dark_oak_log", 89),

STRIPPED_SPRUCE_LOG(41, "minecraft:stripped_spruce_log", 92),

STRIPPED_BIRCH_LOG(42, "minecraft:stripped_birch_log", 95),

STRIPPED_JUNGLE_LOG(43, "minecraft:stripped_jungle_log", 98),

STRIPPED_ACACIA_LOG(44, "minecraft:stripped_acacia_log", 101),

STRIPPED_DARK_OAK_LOG(45, "minecraft:stripped_dark_oak_log", 104),

STRIPPED_OAK_LOG(46, "minecraft:stripped_oak_log", 107),

OAK_WOOD(47, "minecraft:oak_wood", 110),

SPRUCE_WOOD(48, "minecraft:spruce_wood", 113),

BIRCH_WOOD(49, "minecraft:birch_wood", 116),

JUNGLE_WOOD(50, "minecraft:jungle_wood", 119),

ACACIA_WOOD(51, "minecraft:acacia_wood", 122),

DARK_OAK_WOOD(52, "minecraft:dark_oak_wood", 125),

STRIPPED_OAK_WOOD(53, "minecraft:stripped_oak_wood", 128),

STRIPPED_SPRUCE_WOOD(54, "minecraft:stripped_spruce_wood", 131),

STRIPPED_BIRCH_WOOD(55, "minecraft:stripped_birch_wood", 134),

STRIPPED_JUNGLE_WOOD(56, "minecraft:stripped_jungle_wood", 137),

STRIPPED_ACACIA_WOOD(57, "minecraft:stripped_acacia_wood", 140),

STRIPPED_DARK_OAK_WOOD(58, "minecraft:stripped_dark_oak_wood", 143),

OAK_LEAVES(59, "minecraft:oak_leaves", 158),

SPRUCE_LEAVES(60, "minecraft:spruce_leaves", 172),

BIRCH_LEAVES(61, "minecraft:birch_leaves", 186),

JUNGLE_LEAVES(62, "minecraft:jungle_leaves", 200),

ACACIA_LEAVES(63, "minecraft:acacia_leaves", 214),

DARK_OAK_LEAVES(64, "minecraft:dark_oak_leaves", 228),

SPONGE(65, "minecraft:sponge", 229),

WET_SPONGE(66, "minecraft:wet_sponge", 230),

GLASS(67, "minecraft:glass", 231),

LAPIS_ORE(68, "minecraft:lapis_ore", 232),

LAPIS_BLOCK(69, "minecraft:lapis_block", 233),

DISPENSER(70, "minecraft:dispenser", 235),

SANDSTONE(71, "minecraft:sandstone", 246),

CHISELED_SANDSTONE(72, "minecraft:chiseled_sandstone", 247),

CUT_SANDSTONE(73, "minecraft:cut_sandstone", 248),

NOTE_BLOCK(74, "minecraft:note_block", 250),

WHITE_BED(75, "minecraft:white_bed", 1052),

ORANGE_BED(76, "minecraft:orange_bed", 1068),

MAGENTA_BED(77, "minecraft:magenta_bed", 1084),

LIGHT_BLUE_BED(78, "minecraft:light_blue_bed", 1100),

YELLOW_BED(79, "minecraft:yellow_bed", 1116),

LIME_BED(80, "minecraft:lime_bed", 1132),

PINK_BED(81, "minecraft:pink_bed", 1148),

GRAY_BED(82, "minecraft:gray_bed", 1164),

LIGHT_GRAY_BED(83, "minecraft:light_gray_bed", 1180),

CYAN_BED(84, "minecraft:cyan_bed", 1196),

PURPLE_BED(85, "minecraft:purple_bed", 1212),

BLUE_BED(86, "minecraft:blue_bed", 1228),

BROWN_BED(87, "minecraft:brown_bed", 1244),

GREEN_BED(88, "minecraft:green_bed", 1260),

RED_BED(89, "minecraft:red_bed", 1276),

BLACK_BED(90, "minecraft:black_bed", 1292),

POWERED_RAIL(91, "minecraft:powered_rail", 1311),

DETECTOR_RAIL(92, "minecraft:detector_rail", 1323),

STICKY_PISTON(93, "minecraft:sticky_piston", 1335),

COBWEB(94, "minecraft:cobweb", 1341),

GRASS(95, "minecraft:grass", 1342),

FERN(96, "minecraft:fern", 1343),

DEAD_BUSH(97, "minecraft:dead_bush", 1344),

SEAGRASS(98, "minecraft:seagrass", 1345),

TALL_SEAGRASS(99, "minecraft:tall_seagrass", 1347),

PISTON(100, "minecraft:piston", 1354),

PISTON_HEAD(101, "minecraft:piston_head", 1362),

WHITE_WOOL(102, "minecraft:white_wool", 1384),

ORANGE_WOOL(103, "minecraft:orange_wool", 1385),

MAGENTA_WOOL(104, "minecraft:magenta_wool", 1386),

LIGHT_BLUE_WOOL(105, "minecraft:light_blue_wool", 1387),

YELLOW_WOOL(106, "minecraft:yellow_wool", 1388),

LIME_WOOL(107, "minecraft:lime_wool", 1389),

PINK_WOOL(108, "minecraft:pink_wool", 1390),

GRAY_WOOL(109, "minecraft:gray_wool", 1391),

LIGHT_GRAY_WOOL(110, "minecraft:light_gray_wool", 1392),

CYAN_WOOL(111, "minecraft:cyan_wool", 1393),

PURPLE_WOOL(112, "minecraft:purple_wool", 1394),

BLUE_WOOL(113, "minecraft:blue_wool", 1395),

BROWN_WOOL(114, "minecraft:brown_wool", 1396),

GREEN_WOOL(115, "minecraft:green_wool", 1397),

RED_WOOL(116, "minecraft:red_wool", 1398),

BLACK_WOOL(117, "minecraft:black_wool", 1399),

MOVING_PISTON(118, "minecraft:moving_piston", 1400),

DANDELION(119, "minecraft:dandelion", 1412),

POPPY(120, "minecraft:poppy", 1413),

BLUE_ORCHID(121, "minecraft:blue_orchid", 1414),

ALLIUM(122, "minecraft:allium", 1415),

AZURE_BLUET(123, "minecraft:azure_bluet", 1416),

RED_TULIP(124, "minecraft:red_tulip", 1417),

ORANGE_TULIP(125, "minecraft:orange_tulip", 1418),

WHITE_TULIP(126, "minecraft:white_tulip", 1419),

PINK_TULIP(127, "minecraft:pink_tulip", 1420),

OXEYE_DAISY(128, "minecraft:oxeye_daisy", 1421),

CORNFLOWER(129, "minecraft:cornflower", 1422),

WITHER_ROSE(130, "minecraft:wither_rose", 1423),

LILY_OF_THE_VALLEY(131, "minecraft:lily_of_the_valley", 1424),

BROWN_MUSHROOM(132, "minecraft:brown_mushroom", 1425),

RED_MUSHROOM(133, "minecraft:red_mushroom", 1426),

GOLD_BLOCK(134, "minecraft:gold_block", 1427),

IRON_BLOCK(135, "minecraft:iron_block", 1428),

BRICKS(136, "minecraft:bricks", 1429),

TNT(137, "minecraft:tnt", 1431),

BOOKSHELF(138, "minecraft:bookshelf", 1432),

MOSSY_COBBLESTONE(139, "minecraft:mossy_cobblestone", 1433),

OBSIDIAN(140, "minecraft:obsidian", 1434),

TORCH(141, "minecraft:torch", 1435),

WALL_TORCH(142, "minecraft:wall_torch", 1436),

FIRE(143, "minecraft:fire", 1471),

SOUL_FIRE(144, "minecraft:soul_fire", 1952),

SPAWNER(145, "minecraft:spawner", 1953),

OAK_STAIRS(146, "minecraft:oak_stairs", 1965),

CHEST(147, "minecraft:chest", 2035),

REDSTONE_WIRE(148, "minecraft:redstone_wire", 3218),

DIAMOND_ORE(149, "minecraft:diamond_ore", 3354),

DIAMOND_BLOCK(150, "minecraft:diamond_block", 3355),

CRAFTING_TABLE(151, "minecraft:crafting_table", 3356),

WHEAT(152, "minecraft:wheat", 3357),

FARMLAND(153, "minecraft:farmland", 3365),

FURNACE(154, "minecraft:furnace", 3374),

OAK_SIGN(155, "minecraft:oak_sign", 3382),

SPRUCE_SIGN(156, "minecraft:spruce_sign", 3414),

BIRCH_SIGN(157, "minecraft:birch_sign", 3446),

ACACIA_SIGN(158, "minecraft:acacia_sign", 3478),

JUNGLE_SIGN(159, "minecraft:jungle_sign", 3510),

DARK_OAK_SIGN(160, "minecraft:dark_oak_sign", 3542),

OAK_DOOR(161, "minecraft:oak_door", 3584),

LADDER(162, "minecraft:ladder", 3638),

RAIL(163, "minecraft:rail", 3645),

COBBLESTONE_STAIRS(164, "minecraft:cobblestone_stairs", 3666),

OAK_WALL_SIGN(165, "minecraft:oak_wall_sign", 3736),

SPRUCE_WALL_SIGN(166, "minecraft:spruce_wall_sign", 3744),

BIRCH_WALL_SIGN(167, "minecraft:birch_wall_sign", 3752),

ACACIA_WALL_SIGN(168, "minecraft:acacia_wall_sign", 3760),

JUNGLE_WALL_SIGN(169, "minecraft:jungle_wall_sign", 3768),

DARK_OAK_WALL_SIGN(170, "minecraft:dark_oak_wall_sign", 3776),

LEVER(171, "minecraft:lever", 3792),

STONE_PRESSURE_PLATE(172, "minecraft:stone_pressure_plate", 3808),

IRON_DOOR(173, "minecraft:iron_door", 3820),

OAK_PRESSURE_PLATE(174, "minecraft:oak_pressure_plate", 3874),

SPRUCE_PRESSURE_PLATE(175, "minecraft:spruce_pressure_plate", 3876),

BIRCH_PRESSURE_PLATE(176, "minecraft:birch_pressure_plate", 3878),

JUNGLE_PRESSURE_PLATE(177, "minecraft:jungle_pressure_plate", 3880),

ACACIA_PRESSURE_PLATE(178, "minecraft:acacia_pressure_plate", 3882),

DARK_OAK_PRESSURE_PLATE(179, "minecraft:dark_oak_pressure_plate", 3884),

REDSTONE_ORE(180, "minecraft:redstone_ore", 3886),

REDSTONE_TORCH(181, "minecraft:redstone_torch", 3887),

REDSTONE_WALL_TORCH(182, "minecraft:redstone_wall_torch", 3889),

STONE_BUTTON(183, "minecraft:stone_button", 3906),

SNOW(184, "minecraft:snow", 3921),

ICE(185, "minecraft:ice", 3929),

SNOW_BLOCK(186, "minecraft:snow_block", 3930),

CACTUS(187, "minecraft:cactus", 3931),

CLAY(188, "minecraft:clay", 3947),

SUGAR_CANE(189, "minecraft:sugar_cane", 3948),

JUKEBOX(190, "minecraft:jukebox", 3965),

OAK_FENCE(191, "minecraft:oak_fence", 3997),

PUMPKIN(192, "minecraft:pumpkin", 3998),

NETHERRACK(193, "minecraft:netherrack", 3999),

SOUL_SAND(194, "minecraft:soul_sand", 4000),

SOUL_SOIL(195, "minecraft:soul_soil", 4001),

BASALT(196, "minecraft:basalt", 4003),

POLISHED_BASALT(197, "minecraft:polished_basalt", 4006),

SOUL_TORCH(198, "minecraft:soul_torch", 4008),

SOUL_WALL_TORCH(199, "minecraft:soul_wall_torch", 4009),

GLOWSTONE(200, "minecraft:glowstone", 4013),

NETHER_PORTAL(201, "minecraft:nether_portal", 4014),

CARVED_PUMPKIN(202, "minecraft:carved_pumpkin", 4016),

JACK_O_LANTERN(203, "minecraft:jack_o_lantern", 4020),

CAKE(204, "minecraft:cake", 4024),

REPEATER(205, "minecraft:repeater", 4034),

WHITE_STAINED_GLASS(206, "minecraft:white_stained_glass", 4095),

ORANGE_STAINED_GLASS(207, "minecraft:orange_stained_glass", 4096),

MAGENTA_STAINED_GLASS(208, "minecraft:magenta_stained_glass", 4097),

LIGHT_BLUE_STAINED_GLASS(209, "minecraft:light_blue_stained_glass", 4098),

YELLOW_STAINED_GLASS(210, "minecraft:yellow_stained_glass", 4099),

LIME_STAINED_GLASS(211, "minecraft:lime_stained_glass", 4100),

PINK_STAINED_GLASS(212, "minecraft:pink_stained_glass", 4101),

GRAY_STAINED_GLASS(213, "minecraft:gray_stained_glass", 4102),

LIGHT_GRAY_STAINED_GLASS(214, "minecraft:light_gray_stained_glass", 4103),

CYAN_STAINED_GLASS(215, "minecraft:cyan_stained_glass", 4104),

PURPLE_STAINED_GLASS(216, "minecraft:purple_stained_glass", 4105),

BLUE_STAINED_GLASS(217, "minecraft:blue_stained_glass", 4106),

BROWN_STAINED_GLASS(218, "minecraft:brown_stained_glass", 4107),

GREEN_STAINED_GLASS(219, "minecraft:green_stained_glass", 4108),

RED_STAINED_GLASS(220, "minecraft:red_stained_glass", 4109),

BLACK_STAINED_GLASS(221, "minecraft:black_stained_glass", 4110),

OAK_TRAPDOOR(222, "minecraft:oak_trapdoor", 4126),

SPRUCE_TRAPDOOR(223, "minecraft:spruce_trapdoor", 4190),

BIRCH_TRAPDOOR(224, "minecraft:birch_trapdoor", 4254),

JUNGLE_TRAPDOOR(225, "minecraft:jungle_trapdoor", 4318),

ACACIA_TRAPDOOR(226, "minecraft:acacia_trapdoor", 4382),

DARK_OAK_TRAPDOOR(227, "minecraft:dark_oak_trapdoor", 4446),

STONE_BRICKS(228, "minecraft:stone_bricks", 4495),

MOSSY_STONE_BRICKS(229, "minecraft:mossy_stone_bricks", 4496),

CRACKED_STONE_BRICKS(230, "minecraft:cracked_stone_bricks", 4497),

CHISELED_STONE_BRICKS(231, "minecraft:chiseled_stone_bricks", 4498),

INFESTED_STONE(232, "minecraft:infested_stone", 4499),

INFESTED_COBBLESTONE(233, "minecraft:infested_cobblestone", 4500),

INFESTED_STONE_BRICKS(234, "minecraft:infested_stone_bricks", 4501),

INFESTED_MOSSY_STONE_BRICKS(235, "minecraft:infested_mossy_stone_bricks", 4502),

INFESTED_CRACKED_STONE_BRICKS(236, "minecraft:infested_cracked_stone_bricks", 4503),

INFESTED_CHISELED_STONE_BRICKS(237, "minecraft:infested_chiseled_stone_bricks", 4504),

BROWN_MUSHROOM_BLOCK(238, "minecraft:brown_mushroom_block", 4505),

RED_MUSHROOM_BLOCK(239, "minecraft:red_mushroom_block", 4569),

MUSHROOM_STEM(240, "minecraft:mushroom_stem", 4633),

IRON_BARS(241, "minecraft:iron_bars", 4728),

CHAIN(242, "minecraft:chain", 4732),

GLASS_PANE(243, "minecraft:glass_pane", 4766),

MELON(244, "minecraft:melon", 4767),

ATTACHED_PUMPKIN_STEM(245, "minecraft:attached_pumpkin_stem", 4768),

ATTACHED_MELON_STEM(246, "minecraft:attached_melon_stem", 4772),

PUMPKIN_STEM(247, "minecraft:pumpkin_stem", 4776),

MELON_STEM(248, "minecraft:melon_stem", 4784),

VINE(249, "minecraft:vine", 4823),

OAK_FENCE_GATE(250, "minecraft:oak_fence_gate", 4831),

BRICK_STAIRS(251, "minecraft:brick_stairs", 4867),

STONE_BRICK_STAIRS(252, "minecraft:stone_brick_stairs", 4947),

MYCELIUM(253, "minecraft:mycelium", 5017),

LILY_PAD(254, "minecraft:lily_pad", 5018),

NETHER_BRICKS(255, "minecraft:nether_bricks", 5019),

NETHER_BRICK_FENCE(256, "minecraft:nether_brick_fence", 5051),

NETHER_BRICK_STAIRS(257, "minecraft:nether_brick_stairs", 5063),

NETHER_WART(258, "minecraft:nether_wart", 5132),

ENCHANTING_TABLE(259, "minecraft:enchanting_table", 5136),

BREWING_STAND(260, "minecraft:brewing_stand", 5144),

CAULDRON(261, "minecraft:cauldron", 5145),

END_PORTAL(262, "minecraft:end_portal", 5149),

END_PORTAL_FRAME(263, "minecraft:end_portal_frame", 5154),

END_STONE(264, "minecraft:end_stone", 5158),

DRAGON_EGG(265, "minecraft:dragon_egg", 5159),

REDSTONE_LAMP(266, "minecraft:redstone_lamp", 5161),

COCOA(267, "minecraft:cocoa", 5162),

SANDSTONE_STAIRS(268, "minecraft:sandstone_stairs", 5185),

EMERALD_ORE(269, "minecraft:emerald_ore", 5254),

ENDER_CHEST(270, "minecraft:ender_chest", 5256),

TRIPWIRE_HOOK(271, "minecraft:tripwire_hook", 5272),

TRIPWIRE(272, "minecraft:tripwire", 5406),

EMERALD_BLOCK(273, "minecraft:emerald_block", 5407),

SPRUCE_STAIRS(274, "minecraft:spruce_stairs", 5419),

BIRCH_STAIRS(275, "minecraft:birch_stairs", 5499),

JUNGLE_STAIRS(276, "minecraft:jungle_stairs", 5579),

COMMAND_BLOCK(277, "minecraft:command_block", 5654),

BEACON(278, "minecraft:beacon", 5660),

COBBLESTONE_WALL(279, "minecraft:cobblestone_wall", 5664),

MOSSY_COBBLESTONE_WALL(280, "minecraft:mossy_cobblestone_wall", 5988),

FLOWER_POT(281, "minecraft:flower_pot", 6309),

POTTED_OAK_SAPLING(282, "minecraft:potted_oak_sapling", 6310),

POTTED_SPRUCE_SAPLING(283, "minecraft:potted_spruce_sapling", 6311),

POTTED_BIRCH_SAPLING(284, "minecraft:potted_birch_sapling", 6312),

POTTED_JUNGLE_SAPLING(285, "minecraft:potted_jungle_sapling", 6313),

POTTED_ACACIA_SAPLING(286, "minecraft:potted_acacia_sapling", 6314),

POTTED_DARK_OAK_SAPLING(287, "minecraft:potted_dark_oak_sapling", 6315),

POTTED_FERN(288, "minecraft:potted_fern", 6316),

POTTED_DANDELION(289, "minecraft:potted_dandelion", 6317),

POTTED_POPPY(290, "minecraft:potted_poppy", 6318),

POTTED_BLUE_ORCHID(291, "minecraft:potted_blue_orchid", 6319),

POTTED_ALLIUM(292, "minecraft:potted_allium", 6320),

POTTED_AZURE_BLUET(293, "minecraft:potted_azure_bluet", 6321),

POTTED_RED_TULIP(294, "minecraft:potted_red_tulip", 6322),

POTTED_ORANGE_TULIP(295, "minecraft:potted_orange_tulip", 6323),

POTTED_WHITE_TULIP(296, "minecraft:potted_white_tulip", 6324),

POTTED_PINK_TULIP(297, "minecraft:potted_pink_tulip", 6325),

POTTED_OXEYE_DAISY(298, "minecraft:potted_oxeye_daisy", 6326),

POTTED_CORNFLOWER(299, "minecraft:potted_cornflower", 6327),

POTTED_LILY_OF_THE_VALLEY(300, "minecraft:potted_lily_of_the_valley", 6328),

POTTED_WITHER_ROSE(301, "minecraft:potted_wither_rose", 6329),

POTTED_RED_MUSHROOM(302, "minecraft:potted_red_mushroom", 6330),

POTTED_BROWN_MUSHROOM(303, "minecraft:potted_brown_mushroom", 6331),

POTTED_DEAD_BUSH(304, "minecraft:potted_dead_bush", 6332),

POTTED_CACTUS(305, "minecraft:potted_cactus", 6333),

CARROTS(306, "minecraft:carrots", 6334),

POTATOES(307, "minecraft:potatoes", 6342),

OAK_BUTTON(308, "minecraft:oak_button", 6359),

SPRUCE_BUTTON(309, "minecraft:spruce_button", 6383),

BIRCH_BUTTON(310, "minecraft:birch_button", 6407),

JUNGLE_BUTTON(311, "minecraft:jungle_button", 6431),

ACACIA_BUTTON(312, "minecraft:acacia_button", 6455),

DARK_OAK_BUTTON(313, "minecraft:dark_oak_button", 6479),

SKELETON_SKULL(314, "minecraft:skeleton_skull", 6494),

SKELETON_WALL_SKULL(315, "minecraft:skeleton_wall_skull", 6510),

WITHER_SKELETON_SKULL(316, "minecraft:wither_skeleton_skull", 6514),

WITHER_SKELETON_WALL_SKULL(317, "minecraft:wither_skeleton_wall_skull", 6530),

ZOMBIE_HEAD(318, "minecraft:zombie_head", 6534),

ZOMBIE_WALL_HEAD(319, "minecraft:zombie_wall_head", 6550),

PLAYER_HEAD(320, "minecraft:player_head", 6554),

PLAYER_WALL_HEAD(321, "minecraft:player_wall_head", 6570),

CREEPER_HEAD(322, "minecraft:creeper_head", 6574),

CREEPER_WALL_HEAD(323, "minecraft:creeper_wall_head", 6590),

DRAGON_HEAD(324, "minecraft:dragon_head", 6594),

DRAGON_WALL_HEAD(325, "minecraft:dragon_wall_head", 6610),

ANVIL(326, "minecraft:anvil", 6614),

CHIPPED_ANVIL(327, "minecraft:chipped_anvil", 6618),

DAMAGED_ANVIL(328, "minecraft:damaged_anvil", 6622),

TRAPPED_CHEST(329, "minecraft:trapped_chest", 6627),

LIGHT_WEIGHTED_PRESSURE_PLATE(330, "minecraft:light_weighted_pressure_plate", 6650),

HEAVY_WEIGHTED_PRESSURE_PLATE(331, "minecraft:heavy_weighted_pressure_plate", 6666),

COMPARATOR(332, "minecraft:comparator", 6683),

DAYLIGHT_DETECTOR(333, "minecraft:daylight_detector", 6714),

REDSTONE_BLOCK(334, "minecraft:redstone_block", 6730),

NETHER_QUARTZ_ORE(335, "minecraft:nether_quartz_ore", 6731),

HOPPER(336, "minecraft:hopper", 6732),

QUARTZ_BLOCK(337, "minecraft:quartz_block", 6742),

CHISELED_QUARTZ_BLOCK(338, "minecraft:chiseled_quartz_block", 6743),

QUARTZ_PILLAR(339, "minecraft:quartz_pillar", 6745),

QUARTZ_STAIRS(340, "minecraft:quartz_stairs", 6758),

ACTIVATOR_RAIL(341, "minecraft:activator_rail", 6833),

DROPPER(342, "minecraft:dropper", 6840),

WHITE_TERRACOTTA(343, "minecraft:white_terracotta", 6851),

ORANGE_TERRACOTTA(344, "minecraft:orange_terracotta", 6852),

MAGENTA_TERRACOTTA(345, "minecraft:magenta_terracotta", 6853),

LIGHT_BLUE_TERRACOTTA(346, "minecraft:light_blue_terracotta", 6854),

YELLOW_TERRACOTTA(347, "minecraft:yellow_terracotta", 6855),

LIME_TERRACOTTA(348, "minecraft:lime_terracotta", 6856),

PINK_TERRACOTTA(349, "minecraft:pink_terracotta", 6857),

GRAY_TERRACOTTA(350, "minecraft:gray_terracotta", 6858),

LIGHT_GRAY_TERRACOTTA(351, "minecraft:light_gray_terracotta", 6859),

CYAN_TERRACOTTA(352, "minecraft:cyan_terracotta", 6860),

PURPLE_TERRACOTTA(353, "minecraft:purple_terracotta", 6861),

BLUE_TERRACOTTA(354, "minecraft:blue_terracotta", 6862),

BROWN_TERRACOTTA(355, "minecraft:brown_terracotta", 6863),

GREEN_TERRACOTTA(356, "minecraft:green_terracotta", 6864),

RED_TERRACOTTA(357, "minecraft:red_terracotta", 6865),

BLACK_TERRACOTTA(358, "minecraft:black_terracotta", 6866),

WHITE_STAINED_GLASS_PANE(359, "minecraft:white_stained_glass_pane", 6898),

ORANGE_STAINED_GLASS_PANE(360, "minecraft:orange_stained_glass_pane", 6930),

MAGENTA_STAINED_GLASS_PANE(361, "minecraft:magenta_stained_glass_pane", 6962),

LIGHT_BLUE_STAINED_GLASS_PANE(362, "minecraft:light_blue_stained_glass_pane", 6994),

YELLOW_STAINED_GLASS_PANE(363, "minecraft:yellow_stained_glass_pane", 7026),

LIME_STAINED_GLASS_PANE(364, "minecraft:lime_stained_glass_pane", 7058),

PINK_STAINED_GLASS_PANE(365, "minecraft:pink_stained_glass_pane", 7090),

GRAY_STAINED_GLASS_PANE(366, "minecraft:gray_stained_glass_pane", 7122),

LIGHT_GRAY_STAINED_GLASS_PANE(367, "minecraft:light_gray_stained_glass_pane", 7154),

CYAN_STAINED_GLASS_PANE(368, "minecraft:cyan_stained_glass_pane", 7186),

PURPLE_STAINED_GLASS_PANE(369, "minecraft:purple_stained_glass_pane", 7218),

BLUE_STAINED_GLASS_PANE(370, "minecraft:blue_stained_glass_pane", 7250),

BROWN_STAINED_GLASS_PANE(371, "minecraft:brown_stained_glass_pane", 7282),

GREEN_STAINED_GLASS_PANE(372, "minecraft:green_stained_glass_pane", 7314),

RED_STAINED_GLASS_PANE(373, "minecraft:red_stained_glass_pane", 7346),

BLACK_STAINED_GLASS_PANE(374, "minecraft:black_stained_glass_pane", 7378),

ACACIA_STAIRS(375, "minecraft:acacia_stairs", 7390),

DARK_OAK_STAIRS(376, "minecraft:dark_oak_stairs", 7470),

SLIME_BLOCK(377, "minecraft:slime_block", 7539),

BARRIER(378, "minecraft:barrier", 7540),

IRON_TRAPDOOR(379, "minecraft:iron_trapdoor", 7556),

PRISMARINE(380, "minecraft:prismarine", 7605),

PRISMARINE_BRICKS(381, "minecraft:prismarine_bricks", 7606),

DARK_PRISMARINE(382, "minecraft:dark_prismarine", 7607),

PRISMARINE_STAIRS(383, "minecraft:prismarine_stairs", 7619),

PRISMARINE_BRICK_STAIRS(384, "minecraft:prismarine_brick_stairs", 7699),

DARK_PRISMARINE_STAIRS(385, "minecraft:dark_prismarine_stairs", 7779),

PRISMARINE_SLAB(386, "minecraft:prismarine_slab", 7851),

PRISMARINE_BRICK_SLAB(387, "minecraft:prismarine_brick_slab", 7857),

DARK_PRISMARINE_SLAB(388, "minecraft:dark_prismarine_slab", 7863),

SEA_LANTERN(389, "minecraft:sea_lantern", 7866),

HAY_BLOCK(390, "minecraft:hay_block", 7868),

WHITE_CARPET(391, "minecraft:white_carpet", 7870),

ORANGE_CARPET(392, "minecraft:orange_carpet", 7871),

MAGENTA_CARPET(393, "minecraft:magenta_carpet", 7872),

LIGHT_BLUE_CARPET(394, "minecraft:light_blue_carpet", 7873),

YELLOW_CARPET(395, "minecraft:yellow_carpet", 7874),

LIME_CARPET(396, "minecraft:lime_carpet", 7875),

PINK_CARPET(397, "minecraft:pink_carpet", 7876),

GRAY_CARPET(398, "minecraft:gray_carpet", 7877),

LIGHT_GRAY_CARPET(399, "minecraft:light_gray_carpet", 7878),

CYAN_CARPET(400, "minecraft:cyan_carpet", 7879),

PURPLE_CARPET(401, "minecraft:purple_carpet", 7880),

BLUE_CARPET(402, "minecraft:blue_carpet", 7881),

BROWN_CARPET(403, "minecraft:brown_carpet", 7882),

GREEN_CARPET(404, "minecraft:green_carpet", 7883),

RED_CARPET(405, "minecraft:red_carpet", 7884),

BLACK_CARPET(406, "minecraft:black_carpet", 7885),

TERRACOTTA(407, "minecraft:terracotta", 7886),

COAL_BLOCK(408, "minecraft:coal_block", 7887),

PACKED_ICE(409, "minecraft:packed_ice", 7888),

SUNFLOWER(410, "minecraft:sunflower", 7890),

LILAC(411, "minecraft:lilac", 7892),

ROSE_BUSH(412, "minecraft:rose_bush", 7894),

PEONY(413, "minecraft:peony", 7896),

TALL_GRASS(414, "minecraft:tall_grass", 7898),

LARGE_FERN(415, "minecraft:large_fern", 7900),

WHITE_BANNER(416, "minecraft:white_banner", 7901),

ORANGE_BANNER(417, "minecraft:orange_banner", 7917),

MAGENTA_BANNER(418, "minecraft:magenta_banner", 7933),

LIGHT_BLUE_BANNER(419, "minecraft:light_blue_banner", 7949),

YELLOW_BANNER(420, "minecraft:yellow_banner", 7965),

LIME_BANNER(421, "minecraft:lime_banner", 7981),

PINK_BANNER(422, "minecraft:pink_banner", 7997),

GRAY_BANNER(423, "minecraft:gray_banner", 8013),

LIGHT_GRAY_BANNER(424, "minecraft:light_gray_banner", 8029),

CYAN_BANNER(425, "minecraft:cyan_banner", 8045),

PURPLE_BANNER(426, "minecraft:purple_banner", 8061),

BLUE_BANNER(427, "minecraft:blue_banner", 8077),

BROWN_BANNER(428, "minecraft:brown_banner", 8093),

GREEN_BANNER(429, "minecraft:green_banner", 8109),

RED_BANNER(430, "minecraft:red_banner", 8125),

BLACK_BANNER(431, "minecraft:black_banner", 8141),

WHITE_WALL_BANNER(432, "minecraft:white_wall_banner", 8157),

ORANGE_WALL_BANNER(433, "minecraft:orange_wall_banner", 8161),

MAGENTA_WALL_BANNER(434, "minecraft:magenta_wall_banner", 8165),

LIGHT_BLUE_WALL_BANNER(435, "minecraft:light_blue_wall_banner", 8169),

YELLOW_WALL_BANNER(436, "minecraft:yellow_wall_banner", 8173),

LIME_WALL_BANNER(437, "minecraft:lime_wall_banner", 8177),

PINK_WALL_BANNER(438, "minecraft:pink_wall_banner", 8181),

GRAY_WALL_BANNER(439, "minecraft:gray_wall_banner", 8185),

LIGHT_GRAY_WALL_BANNER(440, "minecraft:light_gray_wall_banner", 8189),

CYAN_WALL_BANNER(441, "minecraft:cyan_wall_banner", 8193),

PURPLE_WALL_BANNER(442, "minecraft:purple_wall_banner", 8197),

BLUE_WALL_BANNER(443, "minecraft:blue_wall_banner", 8201),

BROWN_WALL_BANNER(444, "minecraft:brown_wall_banner", 8205),

GREEN_WALL_BANNER(445, "minecraft:green_wall_banner", 8209),

RED_WALL_BANNER(446, "minecraft:red_wall_banner", 8213),

BLACK_WALL_BANNER(447, "minecraft:black_wall_banner", 8217),

RED_SANDSTONE(448, "minecraft:red_sandstone", 8221),

CHISELED_RED_SANDSTONE(449, "minecraft:chiseled_red_sandstone", 8222),

CUT_RED_SANDSTONE(450, "minecraft:cut_red_sandstone", 8223),

RED_SANDSTONE_STAIRS(451, "minecraft:red_sandstone_stairs", 8235),

OAK_SLAB(452, "minecraft:oak_slab", 8307),

SPRUCE_SLAB(453, "minecraft:spruce_slab", 8313),

BIRCH_SLAB(454, "minecraft:birch_slab", 8319),

JUNGLE_SLAB(455, "minecraft:jungle_slab", 8325),

ACACIA_SLAB(456, "minecraft:acacia_slab", 8331),

DARK_OAK_SLAB(457, "minecraft:dark_oak_slab", 8337),

STONE_SLAB(458, "minecraft:stone_slab", 8343),

SMOOTH_STONE_SLAB(459, "minecraft:smooth_stone_slab", 8349),

SANDSTONE_SLAB(460, "minecraft:sandstone_slab", 8355),

CUT_SANDSTONE_SLAB(461, "minecraft:cut_sandstone_slab", 8361),

PETRIFIED_OAK_SLAB(462, "minecraft:petrified_oak_slab", 8367),

COBBLESTONE_SLAB(463, "minecraft:cobblestone_slab", 8373),

BRICK_SLAB(464, "minecraft:brick_slab", 8379),

STONE_BRICK_SLAB(465, "minecraft:stone_brick_slab", 8385),

NETHER_BRICK_SLAB(466, "minecraft:nether_brick_slab", 8391),

QUARTZ_SLAB(467, "minecraft:quartz_slab", 8397),

RED_SANDSTONE_SLAB(468, "minecraft:red_sandstone_slab", 8403),

CUT_RED_SANDSTONE_SLAB(469, "minecraft:cut_red_sandstone_slab", 8409),

PURPUR_SLAB(470, "minecraft:purpur_slab", 8415),

SMOOTH_STONE(471, "minecraft:smooth_stone", 8418),

SMOOTH_SANDSTONE(472, "minecraft:smooth_sandstone", 8419),

SMOOTH_QUARTZ(473, "minecraft:smooth_quartz", 8420),

SMOOTH_RED_SANDSTONE(474, "minecraft:smooth_red_sandstone", 8421),

SPRUCE_FENCE_GATE(475, "minecraft:spruce_fence_gate", 8429),

BIRCH_FENCE_GATE(476, "minecraft:birch_fence_gate", 8461),

JUNGLE_FENCE_GATE(477, "minecraft:jungle_fence_gate", 8493),

ACACIA_FENCE_GATE(478, "minecraft:acacia_fence_gate", 8525),

DARK_OAK_FENCE_GATE(479, "minecraft:dark_oak_fence_gate", 8557),

SPRUCE_FENCE(480, "minecraft:spruce_fence", 8613),

BIRCH_FENCE(481, "minecraft:birch_fence", 8645),

JUNGLE_FENCE(482, "minecraft:jungle_fence", 8677),

ACACIA_FENCE(483, "minecraft:acacia_fence", 8709),

DARK_OAK_FENCE(484, "minecraft:dark_oak_fence", 8741),

SPRUCE_DOOR(485, "minecraft:spruce_door", 8753),

BIRCH_DOOR(486, "minecraft:birch_door", 8817),

JUNGLE_DOOR(487, "minecraft:jungle_door", 8881),

ACACIA_DOOR(488, "minecraft:acacia_door", 8945),

DARK_OAK_DOOR(489, "minecraft:dark_oak_door", 9009),

END_ROD(490, "minecraft:end_rod", 9066),

CHORUS_PLANT(491, "minecraft:chorus_plant", 9131),

CHORUS_FLOWER(492, "minecraft:chorus_flower", 9132),

PURPUR_BLOCK(493, "minecraft:purpur_block", 9138),

PURPUR_PILLAR(494, "minecraft:purpur_pillar", 9140),

PURPUR_STAIRS(495, "minecraft:purpur_stairs", 9153),

END_STONE_BRICKS(496, "minecraft:end_stone_bricks", 9222),

BEETROOTS(497, "minecraft:beetroots", 9223),

GRASS_PATH(498, "minecraft:grass_path", 9227),

END_GATEWAY(499, "minecraft:end_gateway", 9228),

REPEATING_COMMAND_BLOCK(500, "minecraft:repeating_command_block", 9235),

CHAIN_COMMAND_BLOCK(501, "minecraft:chain_command_block", 9247),

FROSTED_ICE(502, "minecraft:frosted_ice", 9253),

MAGMA_BLOCK(503, "minecraft:magma_block", 9257),

NETHER_WART_BLOCK(504, "minecraft:nether_wart_block", 9258),

RED_NETHER_BRICKS(505, "minecraft:red_nether_bricks", 9259),

BONE_BLOCK(506, "minecraft:bone_block", 9261),

STRUCTURE_VOID(507, "minecraft:structure_void", 9263),

OBSERVER(508, "minecraft:observer", 9269),

SHULKER_BOX(509, "minecraft:shulker_box", 9280),

WHITE_SHULKER_BOX(510, "minecraft:white_shulker_box", 9286),

ORANGE_SHULKER_BOX(511, "minecraft:orange_shulker_box", 9292),

MAGENTA_SHULKER_BOX(512, "minecraft:magenta_shulker_box", 9298),

LIGHT_BLUE_SHULKER_BOX(513, "minecraft:light_blue_shulker_box", 9304),

YELLOW_SHULKER_BOX(514, "minecraft:yellow_shulker_box", 9310),

LIME_SHULKER_BOX(515, "minecraft:lime_shulker_box", 9316),

PINK_SHULKER_BOX(516, "minecraft:pink_shulker_box", 9322),

GRAY_SHULKER_BOX(517, "minecraft:gray_shulker_box", 9328),

LIGHT_GRAY_SHULKER_BOX(518, "minecraft:light_gray_shulker_box", 9334),

CYAN_SHULKER_BOX(519, "minecraft:cyan_shulker_box", 9340),

PURPLE_SHULKER_BOX(520, "minecraft:purple_shulker_box", 9346),

BLUE_SHULKER_BOX(521, "minecraft:blue_shulker_box", 9352),

BROWN_SHULKER_BOX(522, "minecraft:brown_shulker_box", 9358),

GREEN_SHULKER_BOX(523, "minecraft:green_shulker_box", 9364),

RED_SHULKER_BOX(524, "minecraft:red_shulker_box", 9370),

BLACK_SHULKER_BOX(525, "minecraft:black_shulker_box", 9376),

WHITE_GLAZED_TERRACOTTA(526, "minecraft:white_glazed_terracotta", 9378),

ORANGE_GLAZED_TERRACOTTA(527, "minecraft:orange_glazed_terracotta", 9382),

MAGENTA_GLAZED_TERRACOTTA(528, "minecraft:magenta_glazed_terracotta", 9386),

LIGHT_BLUE_GLAZED_TERRACOTTA(529, "minecraft:light_blue_glazed_terracotta", 9390),

YELLOW_GLAZED_TERRACOTTA(530, "minecraft:yellow_glazed_terracotta", 9394),

LIME_GLAZED_TERRACOTTA(531, "minecraft:lime_glazed_terracotta", 9398),

PINK_GLAZED_TERRACOTTA(532, "minecraft:pink_glazed_terracotta", 9402),

GRAY_GLAZED_TERRACOTTA(533, "minecraft:gray_glazed_terracotta", 9406),

LIGHT_GRAY_GLAZED_TERRACOTTA(534, "minecraft:light_gray_glazed_terracotta", 9410),

CYAN_GLAZED_TERRACOTTA(535, "minecraft:cyan_glazed_terracotta", 9414),

PURPLE_GLAZED_TERRACOTTA(536, "minecraft:purple_glazed_terracotta", 9418),

BLUE_GLAZED_TERRACOTTA(537, "minecraft:blue_glazed_terracotta", 9422),

BROWN_GLAZED_TERRACOTTA(538, "minecraft:brown_glazed_terracotta", 9426),

GREEN_GLAZED_TERRACOTTA(539, "minecraft:green_glazed_terracotta", 9430),

RED_GLAZED_TERRACOTTA(540, "minecraft:red_glazed_terracotta", 9434),

BLACK_GLAZED_TERRACOTTA(541, "minecraft:black_glazed_terracotta", 9438),

WHITE_CONCRETE(542, "minecraft:white_concrete", 9442),

ORANGE_CONCRETE(543, "minecraft:orange_concrete", 9443),

MAGENTA_CONCRETE(544, "minecraft:magenta_concrete", 9444),

LIGHT_BLUE_CONCRETE(545, "minecraft:light_blue_concrete", 9445),

YELLOW_CONCRETE(546, "minecraft:yellow_concrete", 9446),

LIME_CONCRETE(547, "minecraft:lime_concrete", 9447),

PINK_CONCRETE(548, "minecraft:pink_concrete", 9448),

GRAY_CONCRETE(549, "minecraft:gray_concrete", 9449),

LIGHT_GRAY_CONCRETE(550, "minecraft:light_gray_concrete", 9450),

CYAN_CONCRETE(551, "minecraft:cyan_concrete", 9451),

PURPLE_CONCRETE(552, "minecraft:purple_concrete", 9452),

BLUE_CONCRETE(553, "minecraft:blue_concrete", 9453),

BROWN_CONCRETE(554, "minecraft:brown_concrete", 9454),

GREEN_CONCRETE(555, "minecraft:green_concrete", 9455),

RED_CONCRETE(556, "minecraft:red_concrete", 9456),

BLACK_CONCRETE(557, "minecraft:black_concrete", 9457),

WHITE_CONCRETE_POWDER(558, "minecraft:white_concrete_powder", 9458),

ORANGE_CONCRETE_POWDER(559, "minecraft:orange_concrete_powder", 9459),

MAGENTA_CONCRETE_POWDER(560, "minecraft:magenta_concrete_powder", 9460),

LIGHT_BLUE_CONCRETE_POWDER(561, "minecraft:light_blue_concrete_powder", 9461),

YELLOW_CONCRETE_POWDER(562, "minecraft:yellow_concrete_powder", 9462),

LIME_CONCRETE_POWDER(563, "minecraft:lime_concrete_powder", 9463),

PINK_CONCRETE_POWDER(564, "minecraft:pink_concrete_powder", 9464),

GRAY_CONCRETE_POWDER(565, "minecraft:gray_concrete_powder", 9465),

LIGHT_GRAY_CONCRETE_POWDER(566, "minecraft:light_gray_concrete_powder", 9466),

CYAN_CONCRETE_POWDER(567, "minecraft:cyan_concrete_powder", 9467),

PURPLE_CONCRETE_POWDER(568, "minecraft:purple_concrete_powder", 9468),

BLUE_CONCRETE_POWDER(569, "minecraft:blue_concrete_powder", 9469),

BROWN_CONCRETE_POWDER(570, "minecraft:brown_concrete_powder", 9470),

GREEN_CONCRETE_POWDER(571, "minecraft:green_concrete_powder", 9471),

RED_CONCRETE_POWDER(572, "minecraft:red_concrete_powder", 9472),

BLACK_CONCRETE_POWDER(573, "minecraft:black_concrete_powder", 9473),

KELP(574, "minecraft:kelp", 9474),

KELP_PLANT(575, "minecraft:kelp_plant", 9500),

DRIED_KELP_BLOCK(576, "minecraft:dried_kelp_block", 9501),

TURTLE_EGG(577, "minecraft:turtle_egg", 9502),

DEAD_TUBE_CORAL_BLOCK(578, "minecraft:dead_tube_coral_block", 9514),

DEAD_BRAIN_CORAL_BLOCK(579, "minecraft:dead_brain_coral_block", 9515),

DEAD_BUBBLE_CORAL_BLOCK(580, "minecraft:dead_bubble_coral_block", 9516),

DEAD_FIRE_CORAL_BLOCK(581, "minecraft:dead_fire_coral_block", 9517),

DEAD_HORN_CORAL_BLOCK(582, "minecraft:dead_horn_coral_block", 9518),

TUBE_CORAL_BLOCK(583, "minecraft:tube_coral_block", 9519),

BRAIN_CORAL_BLOCK(584, "minecraft:brain_coral_block", 9520),

BUBBLE_CORAL_BLOCK(585, "minecraft:bubble_coral_block", 9521),

FIRE_CORAL_BLOCK(586, "minecraft:fire_coral_block", 9522),

HORN_CORAL_BLOCK(587, "minecraft:horn_coral_block", 9523),

DEAD_TUBE_CORAL(588, "minecraft:dead_tube_coral", 9524),

DEAD_BRAIN_CORAL(589, "minecraft:dead_brain_coral", 9526),

DEAD_BUBBLE_CORAL(590, "minecraft:dead_bubble_coral", 9528),

DEAD_FIRE_CORAL(591, "minecraft:dead_fire_coral", 9530),

DEAD_HORN_CORAL(592, "minecraft:dead_horn_coral", 9532),

TUBE_CORAL(593, "minecraft:tube_coral", 9534),

BRAIN_CORAL(594, "minecraft:brain_coral", 9536),

BUBBLE_CORAL(595, "minecraft:bubble_coral", 9538),

FIRE_CORAL(596, "minecraft:fire_coral", 9540),

HORN_CORAL(597, "minecraft:horn_coral", 9542),

DEAD_TUBE_CORAL_FAN(598, "minecraft:dead_tube_coral_fan", 9544),

DEAD_BRAIN_CORAL_FAN(599, "minecraft:dead_brain_coral_fan", 9546),

DEAD_BUBBLE_CORAL_FAN(600, "minecraft:dead_bubble_coral_fan", 9548),

DEAD_FIRE_CORAL_FAN(601, "minecraft:dead_fire_coral_fan", 9550),

DEAD_HORN_CORAL_FAN(602, "minecraft:dead_horn_coral_fan", 9552),

TUBE_CORAL_FAN(603, "minecraft:tube_coral_fan", 9554),

BRAIN_CORAL_FAN(604, "minecraft:brain_coral_fan", 9556),

BUBBLE_CORAL_FAN(605, "minecraft:bubble_coral_fan", 9558),

FIRE_CORAL_FAN(606, "minecraft:fire_coral_fan", 9560),

HORN_CORAL_FAN(607, "minecraft:horn_coral_fan", 9562),

DEAD_TUBE_CORAL_WALL_FAN(608, "minecraft:dead_tube_coral_wall_fan", 9564),

DEAD_BRAIN_CORAL_WALL_FAN(609, "minecraft:dead_brain_coral_wall_fan", 9572),

DEAD_BUBBLE_CORAL_WALL_FAN(610, "minecraft:dead_bubble_coral_wall_fan", 9580),

DEAD_FIRE_CORAL_WALL_FAN(611, "minecraft:dead_fire_coral_wall_fan", 9588),

DEAD_HORN_CORAL_WALL_FAN(612, "minecraft:dead_horn_coral_wall_fan", 9596),

TUBE_CORAL_WALL_FAN(613, "minecraft:tube_coral_wall_fan", 9604),

BRAIN_CORAL_WALL_FAN(614, "minecraft:brain_coral_wall_fan", 9612),

BUBBLE_CORAL_WALL_FAN(615, "minecraft:bubble_coral_wall_fan", 9620),

FIRE_CORAL_WALL_FAN(616, "minecraft:fire_coral_wall_fan", 9628),

HORN_CORAL_WALL_FAN(617, "minecraft:horn_coral_wall_fan", 9636),

SEA_PICKLE(618, "minecraft:sea_pickle", 9644),

BLUE_ICE(619, "minecraft:blue_ice", 9652),

CONDUIT(620, "minecraft:conduit", 9653),

BAMBOO_SAPLING(621, "minecraft:bamboo_sapling", 9655),

BAMBOO(622, "minecraft:bamboo", 9656),

POTTED_BAMBOO(623, "minecraft:potted_bamboo", 9668),

VOID_AIR(624, "minecraft:void_air", 9669),

CAVE_AIR(625, "minecraft:cave_air", 9670),

BUBBLE_COLUMN(626, "minecraft:bubble_column", 9671),

POLISHED_GRANITE_STAIRS(627, "minecraft:polished_granite_stairs", 9684),

SMOOTH_RED_SANDSTONE_STAIRS(628, "minecraft:smooth_red_sandstone_stairs", 9764),

MOSSY_STONE_BRICK_STAIRS(629, "minecraft:mossy_stone_brick_stairs", 9844),

POLISHED_DIORITE_STAIRS(630, "minecraft:polished_diorite_stairs", 9924),

MOSSY_COBBLESTONE_STAIRS(631, "minecraft:mossy_cobblestone_stairs", 10004),

END_STONE_BRICK_STAIRS(632, "minecraft:end_stone_brick_stairs", 10084),

STONE_STAIRS(633, "minecraft:stone_stairs", 10164),

SMOOTH_SANDSTONE_STAIRS(634, "minecraft:smooth_sandstone_stairs", 10244),

SMOOTH_QUARTZ_STAIRS(635, "minecraft:smooth_quartz_stairs", 10324),

GRANITE_STAIRS(636, "minecraft:granite_stairs", 10404),

ANDESITE_STAIRS(637, "minecraft:andesite_stairs", 10484),

RED_NETHER_BRICK_STAIRS(638, "minecraft:red_nether_brick_stairs", 10564),

POLISHED_ANDESITE_STAIRS(639, "minecraft:polished_andesite_stairs", 10644),

DIORITE_STAIRS(640, "minecraft:diorite_stairs", 10724),

POLISHED_GRANITE_SLAB(641, "minecraft:polished_granite_slab", 10796),

SMOOTH_RED_SANDSTONE_SLAB(642, "minecraft:smooth_red_sandstone_slab", 10802),

MOSSY_STONE_BRICK_SLAB(643, "minecraft:mossy_stone_brick_slab", 10808),

POLISHED_DIORITE_SLAB(644, "minecraft:polished_diorite_slab", 10814),

MOSSY_COBBLESTONE_SLAB(645, "minecraft:mossy_cobblestone_slab", 10820),

END_STONE_BRICK_SLAB(646, "minecraft:end_stone_brick_slab", 10826),

SMOOTH_SANDSTONE_SLAB(647, "minecraft:smooth_sandstone_slab", 10832),

SMOOTH_QUARTZ_SLAB(648, "minecraft:smooth_quartz_slab", 10838),

GRANITE_SLAB(649, "minecraft:granite_slab", 10844),

ANDESITE_SLAB(650, "minecraft:andesite_slab", 10850),

RED_NETHER_BRICK_SLAB(651, "minecraft:red_nether_brick_slab", 10856),

POLISHED_ANDESITE_SLAB(652, "minecraft:polished_andesite_slab", 10862),

DIORITE_SLAB(653, "minecraft:diorite_slab", 10868),

BRICK_WALL(654, "minecraft:brick_wall", 10874),

PRISMARINE_WALL(655, "minecraft:prismarine_wall", 11198),

RED_SANDSTONE_WALL(656, "minecraft:red_sandstone_wall", 11522),

MOSSY_STONE_BRICK_WALL(657, "minecraft:mossy_stone_brick_wall", 11846),

GRANITE_WALL(658, "minecraft:granite_wall", 12170),

STONE_BRICK_WALL(659, "minecraft:stone_brick_wall", 12494),

NETHER_BRICK_WALL(660, "minecraft:nether_brick_wall", 12818),

ANDESITE_WALL(661, "minecraft:andesite_wall", 13142),

RED_NETHER_BRICK_WALL(662, "minecraft:red_nether_brick_wall", 13466),

SANDSTONE_WALL(663, "minecraft:sandstone_wall", 13790),

END_STONE_BRICK_WALL(664, "minecraft:end_stone_brick_wall", 14114),

DIORITE_WALL(665, "minecraft:diorite_wall", 14438),

SCAFFOLDING(666, "minecraft:scaffolding", 14790),

LOOM(667, "minecraft:loom", 14791),

BARREL(668, "minecraft:barrel", 14796),

SMOKER(669, "minecraft:smoker", 14808),

BLAST_FURNACE(670, "minecraft:blast_furnace", 14816),

CARTOGRAPHY_TABLE(671, "minecraft:cartography_table", 14823),

FLETCHING_TABLE(672, "minecraft:fletching_table", 14824),

GRINDSTONE(673, "minecraft:grindstone", 14829),

LECTERN(674, "minecraft:lectern", 14840),

SMITHING_TABLE(675, "minecraft:smithing_table", 14853),

STONECUTTER(676, "minecraft:stonecutter", 14854),

BELL(677, "minecraft:bell", 14859),

LANTERN(678, "minecraft:lantern", 14893),

SOUL_LANTERN(679, "minecraft:soul_lantern", 14897),

CAMPFIRE(680, "minecraft:campfire", 14901),

SOUL_CAMPFIRE(681, "minecraft:soul_campfire", 14933),

SWEET_BERRY_BUSH(682, "minecraft:sweet_berry_bush", 14962),

WARPED_STEM(683, "minecraft:warped_stem", 14967),

STRIPPED_WARPED_STEM(684, "minecraft:stripped_warped_stem", 14970),

WARPED_HYPHAE(685, "minecraft:warped_hyphae", 14973),

STRIPPED_WARPED_HYPHAE(686, "minecraft:stripped_warped_hyphae", 14976),

WARPED_NYLIUM(687, "minecraft:warped_nylium", 14978),

WARPED_FUNGUS(688, "minecraft:warped_fungus", 14979),

WARPED_WART_BLOCK(689, "minecraft:warped_wart_block", 14980),

WARPED_ROOTS(690, "minecraft:warped_roots", 14981),

NETHER_SPROUTS(691, "minecraft:nether_sprouts", 14982),

CRIMSON_STEM(692, "minecraft:crimson_stem", 14984),

STRIPPED_CRIMSON_STEM(693, "minecraft:stripped_crimson_stem", 14987),

CRIMSON_HYPHAE(694, "minecraft:crimson_hyphae", 14990),

STRIPPED_CRIMSON_HYPHAE(695, "minecraft:stripped_crimson_hyphae", 14993),

CRIMSON_NYLIUM(696, "minecraft:crimson_nylium", 14995),

CRIMSON_FUNGUS(697, "minecraft:crimson_fungus", 14996),

SHROOMLIGHT(698, "minecraft:shroomlight", 14997),

WEEPING_VINES(699, "minecraft:weeping_vines", 14998),

WEEPING_VINES_PLANT(700, "minecraft:weeping_vines_plant", 15024),

TWISTING_VINES(701, "minecraft:twisting_vines", 15025),

TWISTING_VINES_PLANT(702, "minecraft:twisting_vines_plant", 15051),

CRIMSON_ROOTS(703, "minecraft:crimson_roots", 15052),

CRIMSON_PLANKS(704, "minecraft:crimson_planks", 15053),

WARPED_PLANKS(705, "minecraft:warped_planks", 15054),

CRIMSON_SLAB(706, "minecraft:crimson_slab", 15058),

WARPED_SLAB(707, "minecraft:warped_slab", 15064),

CRIMSON_PRESSURE_PLATE(708, "minecraft:crimson_pressure_plate", 15068),

WARPED_PRESSURE_PLATE(709, "minecraft:warped_pressure_plate", 15070),

CRIMSON_FENCE(710, "minecraft:crimson_fence", 15102),

WARPED_FENCE(711, "minecraft:warped_fence", 15134),

CRIMSON_TRAPDOOR(712, "minecraft:crimson_trapdoor", 15150),

WARPED_TRAPDOOR(713, "minecraft:warped_trapdoor", 15214),

CRIMSON_FENCE_GATE(714, "minecraft:crimson_fence_gate", 15270),

WARPED_FENCE_GATE(715, "minecraft:warped_fence_gate", 15302),

CRIMSON_STAIRS(716, "minecraft:crimson_stairs", 15338),

WARPED_STAIRS(717, "minecraft:warped_stairs", 15418),

CRIMSON_BUTTON(718, "minecraft:crimson_button", 15496),

WARPED_BUTTON(719, "minecraft:warped_button", 15520),

CRIMSON_DOOR(720, "minecraft:crimson_door", 15546),

WARPED_DOOR(721, "minecraft:warped_door", 15610),

CRIMSON_SIGN(722, "minecraft:crimson_sign", 15664),

WARPED_SIGN(723, "minecraft:warped_sign", 15696),

CRIMSON_WALL_SIGN(724, "minecraft:crimson_wall_sign", 15728),

WARPED_WALL_SIGN(725, "minecraft:warped_wall_sign", 15736),

STRUCTURE_BLOCK(726, "minecraft:structure_block", 15743),

JIGSAW(727, "minecraft:jigsaw", 15757),

COMPOSTER(728, "minecraft:composter", 15759),

TARGET(729, "minecraft:target", 15768),

BEE_NEST(730, "minecraft:bee_nest", 15784),

BEEHIVE(731, "minecraft:beehive", 15808),

HONEY_BLOCK(732, "minecraft:honey_block", 15832),

HONEYCOMB_BLOCK(733, "minecraft:honeycomb_block", 15833),

NETHERITE_BLOCK(734, "minecraft:netherite_block", 15834),

ANCIENT_DEBRIS(735, "minecraft:ancient_debris", 15835),

CRYING_OBSIDIAN(736, "minecraft:crying_obsidian", 15836),

RESPAWN_ANCHOR(737, "minecraft:respawn_anchor", 15837),

POTTED_CRIMSON_FUNGUS(738, "minecraft:potted_crimson_fungus", 15842),

POTTED_WARPED_FUNGUS(739, "minecraft:potted_warped_fungus", 15843),

POTTED_CRIMSON_ROOTS(740, "minecraft:potted_crimson_roots", 15844),

POTTED_WARPED_ROOTS(741, "minecraft:potted_warped_roots", 15845),

LODESTONE(742, "minecraft:lodestone", 15846),

BLACKSTONE(743, "minecraft:blackstone", 15847),

BLACKSTONE_STAIRS(744, "minecraft:blackstone_stairs", 15859),

BLACKSTONE_WALL(745, "minecraft:blackstone_wall", 15931),

BLACKSTONE_SLAB(746, "minecraft:blackstone_slab", 16255),

POLISHED_BLACKSTONE(747, "minecraft:polished_blackstone", 16258),

POLISHED_BLACKSTONE_BRICKS(748, "minecraft:polished_blackstone_bricks", 16259),

CRACKED_POLISHED_BLACKSTONE_BRICKS(749, "minecraft:cracked_polished_blackstone_bricks", 16260),

CHISELED_POLISHED_BLACKSTONE(750, "minecraft:chiseled_polished_blackstone", 16261),

POLISHED_BLACKSTONE_BRICK_SLAB(751, "minecraft:polished_blackstone_brick_slab", 16265),

POLISHED_BLACKSTONE_BRICK_STAIRS(752, "minecraft:polished_blackstone_brick_stairs", 16279),

POLISHED_BLACKSTONE_BRICK_WALL(753, "minecraft:polished_blackstone_brick_wall", 16351),

GILDED_BLACKSTONE(754, "minecraft:gilded_blackstone", 16672),

POLISHED_BLACKSTONE_STAIRS(755, "minecraft:polished_blackstone_stairs", 16684),

POLISHED_BLACKSTONE_SLAB(756, "minecraft:polished_blackstone_slab", 16756),

POLISHED_BLACKSTONE_PRESSURE_PLATE(757, "minecraft:polished_blackstone_pressure_plate", 16760),

POLISHED_BLACKSTONE_BUTTON(758, "minecraft:polished_blackstone_button", 16770),

POLISHED_BLACKSTONE_WALL(759, "minecraft:polished_blackstone_wall", 16788),

CHISELED_NETHER_BRICKS(760, "minecraft:chiseled_nether_bricks", 17109),

CRACKED_NETHER_BRICKS(761, "minecraft:cracked_nether_bricks", 17110),

QUARTZ_BRICKS(762, "minecraft:quartz_bricks", 17111);
        
companion object {
        private val blocksByID = entries.associateBy { it.id }
        private val blocksByName = entries.associateBy { it.minecraftID }
        fun getBlockByID(id: Int): Block? = blocksByID[id]
        fun getBlockByName(name: String): Block? = blocksByName[name]
}

    operator fun invoke(vararg props: Pair<String, Any>): BlockInstance {
        return BlockInstance(this, defaultState, props.toMap())
    }
}
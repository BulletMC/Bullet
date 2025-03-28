package com.aznos.world.blocks

import com.google.gson.JsonParser
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.InputStreamReader

enum class Block(val id: Int) {
    ACACIA_BUTTON(309),
    ACACIA_DOOR(562),
    ACACIA_FENCE(212),
    ACACIA_FENCE_GATE(256),
    ACACIA_LEAVES(73),
    ACACIA_LOG(41),
    ACACIA_PLANKS(19),
    ACACIA_PRESSURE_PLATE(195),
    ACACIA_SAPLING(27),
    ACACIA_SLAB(142),
    ACACIA_STAIRS(369),
    ACACIA_TRAPDOOR(230),
    ACACIA_WOOD(65),
    ACTIVATOR_RAIL(329),
    AIR(0),
    ALLIUM(114),
    ANCIENT_DEBRIS(960),
    ANDESITE(6),
    ANDESITE_SLAB(552),
    ANDESITE_STAIRS(539),
    ANDESITE_WALL(296),
    ANVIL(314),
    AZURE_BLUET(115),
    BAMBOO(135),
    BARREL(936),
    BARRIER(347),
    BASALT(221),
    BEACON(286),
    BEDROCK(29),
    BEE_NEST(953),
    BEEHIVE(954),
    BEETROOT(889),
    BIRCH_BUTTON(307),
    BIRCH_DOOR(560),
    BIRCH_FENCE(210),
    BIRCH_FENCE_GATE(254),
    BIRCH_LEAVES(71),
    BIRCH_LOG(39),
    BIRCH_PLANKS(17),
    BIRCH_PRESSURE_PLATE(193),
    BIRCH_SAPLING(25),
    BIRCH_SLAB(140),
    BIRCH_STAIRS(281),
    BIRCH_TRAPDOOR(228),
    BIRCH_WOOD(63),
    BLACK_CARPET(365),
    BLACK_CONCRETE(479),
    BLACK_CONCRETE_POWDER(495),
    BLACK_GLAZED_TERRACOTTA(463),
    BLACK_STAINED_GLASS(394),
    BLACK_STAINED_GLASS_PANE(410),
    BLACK_TERRACOTTA(346),
    BLACK_WOOL(110),
    BLACKSTONE(963),
    BLAST_FURNACE(938),
    BLAZE_POWDER(753),
    BLUE_CARPET(361),
    BLUE_CONCRETE(475),
    BLUE_CONCRETE_POWDER(491),
    BLUE_GLAZED_TERRACOTTA(459),
    BLUE_ICE(527),
    BLUE_ORCHID(113),
    BLUE_STAINED_GLASS(390),
    BLUE_STAINED_GLASS_PANE(406),
    BLUE_TERRACOTTA(342),
    BLUE_WOOL(106),
    BOOKSHELF(168),
    BOW(574),
    BRAIN_CORAL(508),
    BRAIN_CORAL_BLOCK(503),
    BRAIN_CORAL_FAN(518),
    BREWING_STAND(755),
    BRICK_SLAB(152),
    BRICK_STAIRS(260),
    BRICK_WALL(289),
    BRICKS(166),
    BROWN_CARPET(362),
    BROWN_CONCRETE(476),
    BROWN_CONCRETE_POWDER(492),
    BROWN_GLAZED_TERRACOTTA(460),
    BROWN_MUSHROOM(124),
    BROWN_MUSHROOM_BLOCK(244),
    BROWN_STAINED_GLASS(391),
    BROWN_STAINED_GLASS_PANE(407),
    BROWN_TERRACOTTA(343),
    BROWN_WOOL(107),
    BUBBLE_CORAL(509),
    BUBBLE_CORAL_BLOCK(504),
    BUBBLE_CORAL_FAN(519),
    CACTUS(205),
    CAMPFIRE(949),
    CARTOGRAPHY_TABLE(939),
    CARVED_PUMPKIN(217),
    CAULDRON(756),
    CHAIN(248),
    CHAIN_COMMAND_BLOCK(423),
    CHARCOAL(577),
    CHEST(180),
    CHIPPED_ANVIL(315),
    CHISELED_NETHER_BRICKS(266),
    CHISELED_POLISHED_BLACKSTONE(970),
    CHISELED_RED_SANDSTONE(419),
    CHISELED_SANDSTONE(82),
    CHORUS_FLOWER(174),
    CHORUS_FRUIT(887),
    CHORUS_PLANT(173),
    CLAY(206),
    COAL_BLOCK(367),
    COAL_ORE(35),
    COARSE_DIRT(10),
    COBBLESTONE(14),
    COBWEB(88),
    COCOA_BEANS(694),
    COMMAND_BLOCK(285),
    COMPARATOR(567),
    COMPOSTER(935),
    CONDUIT(528),
    CORNFLOWER(121),
    CRACKED_NETHER_BRICKS(265),
    CRAFTING_TABLE(183),
    CREEPER_HEAD(840),
    CRIMSON_BUTTON(311),
    CRIMSON_DOOR(564),
    CRIMSON_FENCE(214),
    CRIMSON_FENCE_GATE(258),
    CRIMSON_FUNGUS(126),
    CRIMSON_HYPHAE(67),
    CRIMSON_NYLIUM(12),
    CRIMSON_PLANKS(21),
    CRIMSON_PRESSURE_PLATE(197),
    CRIMSON_ROOTS(128),
    CRIMSON_SLAB(144),
    CRIMSON_STAIRS(283),
    CRIMSON_STEM(43),
    CRIMSON_TRAPDOOR(232),
    CRYING_OBSIDIAN(962),
    CUT_RED_SANDSTONE(420),
    CUT_SANDSTONE(83),
    CYAN_CARPET(359),
    CYAN_CONCRETE(473),
    CYAN_CONCRETE_POWDER(489),
    CYAN_GLAZED_TERRACOTTA(457),
    CYAN_STAINED_GLASS(388),
    CYAN_STAINED_GLASS_PANE(404),
    CYAN_TERRACOTTA(340),
    CYAN_WOOL(104),
    DAMAGED_ANVIL(316),
    DANDELION(111),
    DARK_OAK_BUTTON(310),
    DARK_OAK_DOOR(563),
    DARK_OAK_FENCE(213),
    DARK_OAK_FENCE_GATE(257),
    DARK_OAK_LEAVES(74),
    DARK_OAK_LOG(42),
    DARK_OAK_PLANKS(20),
    DARK_OAK_PRESSURE_PLATE(196),
    DARK_OAK_SAPLING(28),
    DARK_OAK_SLAB(143),
    DARK_OAK_STAIRS(370),
    DARK_OAK_TRAPDOOR(231),
    DARK_OAK_WOOD(66),
    DARK_PRISMARINE(413),
    DARK_PRISMARINE_SLAB(161),
    DARK_PRISMARINE_STAIRS(416),
    DAYLIGHT_DETECTOR(320),
    DEAD_BRAIN_CORAL(512),
    DEAD_BRAIN_CORAL_BLOCK(498),
    DEAD_BRAIN_CORAL_FAN(523),
    DEAD_BUBBLE_CORAL(513),
    DEAD_BUBBLE_CORAL_BLOCK(499),
    DEAD_BUBBLE_CORAL_FAN(524),
    DEAD_BUSH(91),
    DEAD_FIRE_CORAL(514),
    DEAD_FIRE_CORAL_BLOCK(500),
    DEAD_FIRE_CORAL_FAN(525),
    DEAD_HORN_CORAL(515),
    DEAD_HORN_CORAL_BLOCK(501),
    DEAD_HORN_CORAL_FAN(526),
    DEAD_TUBE_CORAL(516),
    DEAD_TUBE_CORAL_BLOCK(497),
    DEAD_TUBE_CORAL_FAN(522),
    DETECTOR_RAIL(86),
    DIORITE(4),
    DIORITE_SLAB(555),
    DIORITE_STAIRS(542),
    DIORITE_WALL(300),
    DIRT(9),
    DISPENSER(80),
    DRAGON_BREATH(892),
    DRAGON_HEAD(841),
    DRIED_KELP(736),
    DRIED_KELP_BLOCK(676),
    DROPPER(330),
    EMERALD_BLOCK(279),
    EMERALD_ORE(276),
    ENCHANTING_TABLE(269),
    END_CRYSTAL(886),
    END_PORTAL_FRAME(270),
    END_ROD(172),
    END_STONE(271),
    ENDER_CHEST(277),
    FARMLAND(184),
    FEATHER(617),
    FERMENTED_SPIDER_EYE(752),
    FERN(90),
    FIRE_CORAL(510),
    FIRE_CORAL_BLOCK(505),
    FIRE_CORAL_FAN(520),
    FLETCHING_TABLE(940),
    FLINT(646),
    FLINT_AND_STEEL(572),
    FURNACE(185),
    GILDED_BLACKSTONE(966),
    GLASS(77),
    GLASS_PANE(249),
    GLOWSTONE(224),
    GOLD_BLOCK(136),
    GOLD_INGOT(580),
    GOLD_NUGGET(747),
    GOLD_ORE(33),
    GRANITE(2),
    GRANITE_SLAB(551),
    GRANITE_STAIRS(538),
    GRANITE_WALL(293),
    GRASS(89),
    GRASS_BLOCK(8),
    GRASS_PATH(372),
    GRAVEL(32),
    GRAY_CARPET(357),
    GRAY_CONCRETE(471),
    GRAY_CONCRETE_POWDER(487),
    GRAY_GLAZED_TERRACOTTA(455),
    GRAY_STAINED_GLASS(386),
    GRAY_STAINED_GLASS_PANE(402),
    GRAY_TERRACOTTA(338),
    GRAY_WOOL(102),
    GREEN_CARPET(363),
    GREEN_CONCRETE(477),
    GREEN_CONCRETE_POWDER(493),
    GREEN_GLAZED_TERRACOTTA(461),
    GREEN_STAINED_GLASS(392),
    GREEN_STAINED_GLASS_PANE(408),
    GREEN_TERRACOTTA(344),
    GREEN_WOOL(108),
    GRINDSTONE(941),
    HAY_BLOCK(349),
    HEART_OF_THE_SEA(925),
    HEAVY_WEIGHTED_PRESSURE_PLATE(319),
    HONEY_BLOCK(956),
    HOPPER(323),
    HORN_CORAL(511),
    HORN_CORAL_BLOCK(506),
    HORN_CORAL_FAN(521),
    ICE(203),
    INFESTED_COBBLESTONE(235),
    INFESTED_STONE(234),
    JACK_O_LANTERN(225),
    JIGSAW(569),
    JUKEBOX(207),
    JUNGLE_BUTTON(308),
    JUNGLE_DOOR(561),
    JUNGLE_FENCE(211),
    JUNGLE_FENCE_GATE(255),
    JUNGLE_LEAVES(72),
    JUNGLE_LOG(40),
    JUNGLE_PLANKS(18),
    JUNGLE_PRESSURE_PLATE(194),
    JUNGLE_SAPLING(26),
    JUNGLE_SLAB(141),
    JUNGLE_STAIRS(282),
    JUNGLE_TRAPDOOR(229),
    JUNGLE_WOOD(64),
    KELP(134),
    LADDER(186),
    LANTERN(946),
    LAPIS_BLOCK(79),
    LAPIS_LAZULI(695),
    LAPIS_ORE(78),
    LARGE_FERN(378),
    LECTERN(942),
    LEVER(189),
    LIGHT_BLUE_CARPET(353),
    LIGHT_BLUE_CONCRETE(467),
    LIGHT_BLUE_CONCRETE_POWDER(483),
    LIGHT_BLUE_GLAZED_TERRACOTTA(451),
    LIGHT_BLUE_STAINED_GLASS(382),
    LIGHT_BLUE_STAINED_GLASS_PANE(398),
    LIGHT_BLUE_TERRACOTTA(334),
    LIGHT_BLUE_WOOL(98),
    LIGHT_GRAY_CARPET(358),
    LIGHT_GRAY_CONCRETE(472),
    LIGHT_GRAY_CONCRETE_POWDER(488),
    LIGHT_GRAY_GLAZED_TERRACOTTA(456),
    LIGHT_GRAY_STAINED_GLASS(387),
    LIGHT_GRAY_STAINED_GLASS_PANE(403),
    LIGHT_GRAY_TERRACOTTA(339),
    LIGHT_GRAY_WOOL(103),
    LIGHT_WEIGHTED_PRESSURE_PLATE(318),
    LILAC(374),
    LILY_OF_THE_VALLEY(122),
    LILY_PAD(263),
    LIME_CARPET(355),
    LIME_CONCRETE(469),
    LIME_CONCRETE_POWDER(485),
    LIME_GLAZED_TERRACOTTA(453),
    LIME_STAINED_GLASS(384),
    LIME_STAINED_GLASS_PANE(400),
    LIME_TERRACOTTA(336),
    LIME_WOOL(100),
    LODESTONE(958),
    LOOM(928),
    MAGENTA_CARPET(352),
    MAGENTA_CONCRETE(466),
    MAGENTA_CONCRETE_POWDER(482),
    MAGENTA_GLAZED_TERRACOTTA(450),
    MAGENTA_STAINED_GLASS(381),
    MAGENTA_STAINED_GLASS_PANE(397),
    MAGENTA_TERRACOTTA(333),
    MAGENTA_WOOL(97),
    MAGMA_BLOCK(424),
    MAGMA_CREAM(754),
    MELON(250),
    MOSSY_COBBLESTONE(169),
    MUSHROOM_STEM(246),
    MYCELIUM(262),
    NAUTILUS_SHELL(924),
    NETHER_BRICK(849),
    NETHER_BRICK_FENCE(267),
    NETHER_BRICK_SLAB(154),
    NETHER_BRICK_STAIRS(268),
    NETHER_BRICK_WALL(295),
    NETHER_BRICKS(264),
    NETHER_GOLD_ORE(36),
    NETHER_SPROUTS(130),
    NETHERRACK(218),
    NOTE_BLOCK(84),
    OAK_BUTTON(305),
    OAK_DOOR(558),
    OAK_FENCE(208),
    OAK_FENCE_GATE(252),
    OAK_LEAVES(69),
    OAK_LOG(37),
    OAK_PLANKS(15),
    OAK_PRESSURE_PLATE(191),
    OAK_SAPLING(23),
    OAK_SLAB(138),
    OAK_STAIRS(179),
    OAK_TRAPDOOR(226),
    OAK_WOOD(61),
    OBSERVER(430),
    OBSIDIAN(170),
    ORANGE_CARPET(351),
    ORANGE_CONCRETE(465),
    ORANGE_CONCRETE_POWDER(481),
    ORANGE_GLAZED_TERRACOTTA(449),
    ORANGE_STAINED_GLASS(380),
    ORANGE_STAINED_GLASS_PANE(396),
    ORANGE_TERRACOTTA(332),
    ORANGE_TULIP(117),
    ORANGE_WOOL(96),
    OXEYE_DAISY(120),
    PACKED_ICE(368),
    PEONY(376),
    PETRIFIED_OAK_SLAB(150),
    PINK_CARPET(356),
    PINK_CONCRETE(470),
    PINK_CONCRETE_POWDER(486),
    PINK_GLAZED_TERRACOTTA(454),
    PINK_STAINED_GLASS(385),
    PINK_STAINED_GLASS_PANE(401),
    PINK_TERRACOTTA(337),
    PINK_TULIP(119),
    PINK_WOOL(101),
    PISTON(94),
    PLAYER_HEAD(838),
    PODZOL(11),
    POLISHED_ANDESITE(7),
    POLISHED_ANDESITE_SLAB(554),
    POLISHED_ANDESITE_STAIRS(541),
    POLISHED_BASALT(222),
    POLISHED_BLACKSTONE(967),
    POLISHED_DIORITE(5),
    POLISHED_DIORITE_SLAB(546),
    POLISHED_DIORITE_STAIRS(532),
    POLISHED_GRANITE(3),
    POLISHED_GRANITE_SLAB(543),
    POLISHED_GRANITE_STAIRS(529),
    POPPED_CHORUS_FRUIT(888),
    POPPY(112),
    POWERED_RAIL(85),
    PRISMARINE(411),
    PRISMARINE_BRICK_SLAB(160),
    PRISMARINE_BRICK_STAIRS(415),
    PRISMARINE_BRICKS(412),
    PRISMARINE_CRYSTALS(854),
    PRISMARINE_SHARD(853),
    PRISMARINE_SLAB(159),
    PRISMARINE_STAIRS(414),
    PRISMARINE_WALL(290),
    PUMPKIN(216),
    PUMPKIN_PIE(845),
    PURPLE_CARPET(360),
    PURPLE_CONCRETE(474),
    PURPLE_CONCRETE_POWDER(490),
    PURPLE_GLAZED_TERRACOTTA(458),
    PURPLE_STAINED_GLASS(389),
    PURPLE_STAINED_GLASS_PANE(405),
    PURPLE_TERRACOTTA(341),
    PURPLE_WOOL(105),
    PURPUR_BLOCK(175),
    PURPUR_PILLAR(176),
    PURPUR_SLAB(158),
    PURPUR_STAIRS(177),
    RAIL(187),
    RED_CARPET(364),
    RED_CONCRETE(478),
    RED_CONCRETE_POWDER(494),
    RED_GLAZED_TERRACOTTA(462),
    RED_MUSHROOM(125),
    RED_MUSHROOM_BLOCK(245),
    RED_NETHER_BRICK_SLAB(553),
    RED_NETHER_BRICK_STAIRS(540),
    RED_NETHER_BRICK_WALL(297),
    RED_NETHER_BRICKS(427),
    RED_SAND(31),
    RED_SANDSTONE(418),
    RED_STAINED_GLASS(393),
    RED_STAINED_GLASS_PANE(409),
    RED_TERRACOTTA(345),
    RED_TULIP(116),
    RED_WOOL(109),
    REDSTONE(665),
    REPEATER(566),
    REPEATING_COMMAND_BLOCK(422),
    RESPAWN_ANCHOR(975),
    ROSE_BUSH(375),
    ROTTEN_FLESH(743),
    SAND(30),
    SANDSTONE(81),
    SCAFFOLDING(556),
    SEA_LANTERN(417),
    SEA_PICKLE(93),
    SEAGRASS(92),
    SHROOMLIGHT(951),
    SHULKER_SHELL(905),
    SKELETON_SKULL(836),
    SLIME_BALL(679),
    SLIME_BLOCK(371),
    SMITHING_TABLE(943),
    SMOKER(937),
    SMOOTH_RED_SANDSTONE(163),
    SMOOTH_SANDSTONE(164),
    SMOOTH_STONE(165),
    SNOW(202),
    SNOW_BLOCK(204),
    SOUL_CAMPFIRE(950),
    SOUL_LANTERN(947),
    SOUL_SAND(219),
    SOUL_SOIL(220),
    SPAWNER(178),
    SPIDER_EYE(751),
    SPONGE(75),
    SPRUCE_BUTTON(306),
    SPRUCE_DOOR(559),
    SPRUCE_FENCE(209),
    SPRUCE_FENCE_GATE(253),
    SPRUCE_LEAVES(70),
    SPRUCE_LOG(38),
    SPRUCE_PLANKS(16),
    SPRUCE_PRESSURE_PLATE(192),
    SPRUCE_SAPLING(24),
    SPRUCE_SLAB(139),
    SPRUCE_STAIRS(280),
    SPRUCE_TRAPDOOR(227),
    SPRUCE_WOOD(62),
    STONE(1),
    STONECUTTER(944),
    STRIPPED_ACACIA_LOG(49),
    STRIPPED_ACACIA_WOOD(57),
    STRIPPED_BIRCH_LOG(47),
    STRIPPED_BIRCH_WOOD(55),
    STRIPPED_CRIMSON_HYPHAE(59),
    STRIPPED_CRIMSON_STEM(51),
    STRIPPED_DARK_OAK_LOG(50),
    STRIPPED_DARK_OAK_WOOD(58),
    STRIPPED_JUNGLE_LOG(48),
    STRIPPED_JUNGLE_WOOD(56),
    STRIPPED_OAK_LOG(45),
    STRIPPED_OAK_WOOD(53),
    STRIPPED_SPRUCE_LOG(46),
    STRIPPED_SPRUCE_WOOD(54),
    STRIPPED_WARPED_HYPHAE(60),
    STRIPPED_WARPED_STEM(52),
    STRUCTURE_BLOCK(568),
    STRUCTURE_VOID(429),
    SUNFLOWER(373),
    SWEET_BERRIES(948),
    TALL_GRASS(377),
    TARGET(961),
    TERRACOTTA(366),
    TNT(167),
    TRAPPED_CHEST(317),
    TRIDENT(922),
    TRIPWIRE_HOOK(278),
    TUBE_CORAL(507),
    TUBE_CORAL_BLOCK(502),
    TUBE_CORAL_FAN(517),
    TWISTING_VINES(132),
    VINE(251),
    WARPED_BUTTON(312),
    WARPED_DOOR(565),
    WARPED_FENCE(215),
    WARPED_FENCE_GATE(259),
    WARPED_FUNGUS(127),
    WARPED_HYPHAE(68),
    WARPED_NYLIUM(13),
    WARPED_PLANKS(22),
    WARPED_PRESSURE_PLATE(198),
    WARPED_ROOTS(129),
    WARPED_SLAB(145),
    WARPED_STAIRS(284),
    WARPED_STEM(44),
    WARPED_TRAPDOOR(233),
    WARPED_WART_BLOCK(426),
    WEEPING_VINES(131),
    WET_SPONGE(76),
    WHITE_CARPET(350),
    WHITE_CONCRETE(464),
    WHITE_CONCRETE_POWDER(480),
    WHITE_GLAZED_TERRACOTTA(448),
    WHITE_STAINED_GLASS(379),
    WHITE_STAINED_GLASS_PANE(395),
    WHITE_TERRACOTTA(331),
    WHITE_TULIP(118),
    WHITE_WOOL(95),
    WITHER_ROSE(123),
    WITHER_SKELETON_SKULL(837),
    YELLOW_CARPET(354),
    YELLOW_CONCRETE(468),
    YELLOW_CONCRETE_POWDER(484),
    YELLOW_GLAZED_TERRACOTTA(452),
    YELLOW_STAINED_GLASS(383),
    YELLOW_STAINED_GLASS_PANE(399),
    YELLOW_TERRACOTTA(335),
    YELLOW_WOOL(99),
    ZOMBIE_HEAD(839);

    companion object {
        private val idMap = Int2ObjectOpenHashMap<Block>(Block.entries.size)

        init {
            entries.associateByTo(idMap) { it.id }
        }

        /**
         * Gets a block from its ID
         *
         * @param id The ID of the block to get
         * @return The block with the given ID, or null if no block with that ID exists
         */
        fun getBlockFromID(id: Int): Block? {
            return idMap.get(id)
        }

        /**
         * Converts a global palette ID to a state palette ID
         *
         * @param block The global block palette to get the state ID of
         * @param properties Any optional properties to match against the block's states, for example the
         * grass block has two properties, "snowy" and "default".
         *
         * @return The state ID of the state palette related to the block
         */
        fun getStateID(block: Block, properties: Map<String, String>? = null): Int {
            val jsonStream = Block::class.java.getResourceAsStream("/blocks.json")
                ?: throw IllegalArgumentException("blocks.json not found")

            val reader = InputStreamReader(jsonStream)
            val json = JsonParser.parseReader(reader).asJsonObject
            val blockKey = "minecraft:${block.name.lowercase()}"

            val blockData = json[blockKey]?.asJsonObject ?: throw IllegalArgumentException("Block $blockKey not found")
            val states = blockData["states"]?.asJsonArray ?: throw IllegalArgumentException("Block $blockKey has no states")

            if(properties != null) {
                for(element in states) {
                    val stateObj = element.asJsonObject
                    val stateProps = stateObj["properties"]?.asJsonObject

                    val matches = if(stateProps == null && properties.isEmpty()) {
                        true
                    } else if(stateProps != null) {
                        properties.entries.all { (key, value) ->
                            stateProps.has(key) && stateProps[key].asString == value
                        }
                    } else {
                        false
                    }

                    if(matches) {
                        return stateObj["id"].asInt
                    }
                }
            }

            for(element in states) {
                val stateObj = element.asJsonObject
                if(stateObj["default"]?.asBoolean == true) {
                    return stateObj["id"].asInt
                }
            }

            throw IllegalArgumentException("No matching or default state found for $blockKey")
        }
    }
}
package com.aznos.world.items

import com.aznos.world.blocks.Block
import com.google.gson.JsonParser
import java.io.InputStreamReader

enum class Item(val id: Int) {
    ACACIA_BOAT(902),
    ACACIA_BUTTON(309),
    ACACIA_DOOR(562),
    ACACIA_FENCE(212),
    ACACIA_FENCE_GATE(256),
    ACACIA_LEAVES(73),
    ACACIA_LOG(41),
    ACACIA_PLANKS(19),
    ACACIA_PRESSURE_PLATE(195),
    ACACIA_SAPLING(27),
    ACACIA_SIGN(656),
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
    APPLE(573),
    ARMOR_STAND(860),
    ARROW(575),
    AZURE_BLUET(115),
    BAKED_POTATO(832),
    BAMBOO(135),
    BARREL(936),
    BARRIER(347),
    BASALT(221),
    BAT_SPAWN_EGG(759),
    BEACON(286),
    BEDROCK(29),
    BEE_NEST(953),
    BEE_SPAWN_EGG(760),
    BEEF(739),
    BEEHIVE(954),
    BEETROOT(889),
    BEETROOT_SEEDS(890),
    BEETROOT_SOUP(891),
    BELL(945),
    BIRCH_BOAT(900),
    BIRCH_BUTTON(307),
    BIRCH_DOOR(560),
    BIRCH_FENCE(210),
    BIRCH_FENCE_GATE(254),
    BIRCH_LEAVES(71),
    BIRCH_LOG(39),
    BIRCH_PLANKS(17),
    BIRCH_PRESSURE_PLATE(193),
    BIRCH_SAPLING(25),
    BIRCH_SIGN(654),
    BIRCH_SLAB(140),
    BIRCH_STAIRS(281),
    BIRCH_TRAPDOOR(228),
    BIRCH_WOOD(63),
    BLACK_BANNER(885),
    BLACK_BED(731),
    BLACK_CARPET(365),
    BLACK_CONCRETE(479),
    BLACK_CONCRETE_POWDER(495),
    BLACK_DYE(711),
    BLACK_GLAZED_TERRACOTTA(463),
    BLACK_SHULKER_BOX(447),
    BLACK_STAINED_GLASS(394),
    BLACK_STAINED_GLASS_PANE(410),
    BLACK_TERRACOTTA(346),
    BLACK_WOOL(110),
    BLACKSTONE(963),
    BLACKSTONE_SLAB(964),
    BLACKSTONE_STAIRS(965),
    BLACKSTONE_WALL(301),
    BLAST_FURNACE(938),
    BLAZE_POWDER(753),
    BLAZE_ROD(745),
    BLAZE_SPAWN_EGG(761),
    BLUE_BANNER(881),
    BLUE_BED(727),
    BLUE_CARPET(361),
    BLUE_CONCRETE(475),
    BLUE_CONCRETE_POWDER(491),
    BLUE_DYE(707),
    BLUE_GLAZED_TERRACOTTA(459),
    BLUE_ICE(527),
    BLUE_ORCHID(113),
    BLUE_SHULKER_BOX(443),
    BLUE_STAINED_GLASS(390),
    BLUE_STAINED_GLASS_PANE(406),
    BLUE_TERRACOTTA(342),
    BLUE_WOOL(106),
    BONE(713),
    BONE_BLOCK(428),
    BONE_MEAL(712),
    BOOK(678),
    BOOKSHELF(168),
    BOW(574),
    BOWL(614),
    BRAIN_CORAL(508),
    BRAIN_CORAL_BLOCK(503),
    BRAIN_CORAL_FAN(518),
    BREAD(621),
    BREWING_STAND(755),
    BRICK(674),
    BRICK_SLAB(152),
    BRICK_STAIRS(260),
    BRICK_WALL(289),
    BRICKS(166),
    BROWN_BANNER(882),
    BROWN_BED(728),
    BROWN_CARPET(362),
    BROWN_CONCRETE(476),
    BROWN_CONCRETE_POWDER(492),
    BROWN_DYE(708),
    BROWN_GLAZED_TERRACOTTA(460),
    BROWN_MUSHROOM(124),
    BROWN_MUSHROOM_BLOCK(244),
    BROWN_SHULKER_BOX(444),
    BROWN_STAINED_GLASS(391),
    BROWN_STAINED_GLASS_PANE(407),
    BROWN_TERRACOTTA(343),
    BROWN_WOOL(107),
    BUBBLE_CORAL(509),
    BUBBLE_CORAL_BLOCK(504),
    BUBBLE_CORAL_FAN(519),
    BUCKET(660),
    CACTUS(205),
    CAKE(715),
    CAMPFIRE(949),
    CARROT(830),
    CARROT_ON_A_STICK(842),
    CARTOGRAPHY_TABLE(939),
    CARVED_PUMPKIN(217),
    CAT_SPAWN_EGG(762),
    CAULDRON(756),
    CAVE_SPIDER_SPAWN_EGG(763),
    CHAIN(248),
    CHAIN_COMMAND_BLOCK(423),
    CHAINMAIL_BOOTS(629),
    CHAINMAIL_CHESTPLATE(627),
    CHAINMAIL_HELMET(626),
    CHAINMAIL_LEGGINGS(628),
    CHARCOAL(577),
    CHEST(180),
    CHEST_MINECART(680),
    CHICKEN(741),
    CHICKEN_SPAWN_EGG(764),
    CHIPPED_ANVIL(315),
    CHISELED_NETHER_BRICKS(266),
    CHISELED_POLISHED_BLACKSTONE(970),
    CHISELED_QUARTZ_BLOCK(324),
    CHISELED_RED_SANDSTONE(419),
    CHISELED_SANDSTONE(82),
    CHISELED_STONE_BRICKS(243),
    CHORUS_FLOWER(174),
    CHORUS_FRUIT(887),
    CHORUS_PLANT(173),
    CLAY(206),
    CLAY_BALL(675),
    CLOCK(685),
    COAL(576),
    COAL_BLOCK(367),
    COAL_ORE(35),
    COARSE_DIRT(10),
    COBBLESTONE(14),
    COBBLESTONE_SLAB(151),
    COBBLESTONE_STAIRS(188),
    COBBLESTONE_WALL(287),
    COBWEB(88),
    COCOA_BEANS(694),
    COD(687),
    COD_BUCKET(672),
    COD_SPAWN_EGG(765),
    COMMAND_BLOCK(285),
    COMMAND_BLOCK_MINECART(867),
    COMPARATOR(567),
    COMPASS(683),
    COMPOSTER(935),
    CONDUIT(528),
    COOKED_BEEF(740),
    COOKED_CHICKEN(742),
    COOKED_COD(691),
    COOKED_MUTTON(869),
    COOKED_PORKCHOP(648),
    COOKED_RABBIT(856),
    COOKED_SALMON(692),
    COOKIE(732),
    CORNFLOWER(121),
    COW_SPAWN_EGG(766),
    CRACKED_NETHER_BRICKS(265),
    CRACKED_POLISHED_BLACKSTONE_BRICKS(974),
    CRACKED_STONE_BRICKS(242),
    CRAFTING_TABLE(183),
    CREEPER_BANNER_PATTERN(930),
    CREEPER_HEAD(840),
    CREEPER_SPAWN_EGG(767),
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
    CRIMSON_SIGN(658),
    CRIMSON_SLAB(144),
    CRIMSON_STAIRS(283),
    CRIMSON_STEM(43),
    CRIMSON_TRAPDOOR(232),
    CROSSBOW(926),
    CRYING_OBSIDIAN(962),
    CUT_RED_SANDSTONE(420),
    CUT_RED_SANDSTONE_SLAB(157),
    CUT_SANDSTONE(83),
    CUT_SANDSTONE_SLAB(149),
    CYAN_BANNER(879),
    CYAN_BED(725),
    CYAN_CARPET(359),
    CYAN_CONCRETE(473),
    CYAN_CONCRETE_POWDER(489),
    CYAN_DYE(705),
    CYAN_GLAZED_TERRACOTTA(457),
    CYAN_SHULKER_BOX(441),
    CYAN_STAINED_GLASS(388),
    CYAN_STAINED_GLASS_PANE(404),
    CYAN_TERRACOTTA(340),
    CYAN_WOOL(104),
    DAMAGED_ANVIL(316),
    DANDELION(111),
    DARK_OAK_BOAT(903),
    DARK_OAK_BUTTON(310),
    DARK_OAK_DOOR(563),
    DARK_OAK_FENCE(213),
    DARK_OAK_FENCE_GATE(257),
    DARK_OAK_LEAVES(74),
    DARK_OAK_LOG(42),
    DARK_OAK_PLANKS(20),
    DARK_OAK_PRESSURE_PLATE(196),
    DARK_OAK_SAPLING(28),
    DARK_OAK_SIGN(657),
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
    DEBUG_STICK(908),
    DETECTOR_RAIL(86),
    DIAMOND(578),
    DIAMOND_AXE(606),
    DIAMOND_BLOCK(182),
    DIAMOND_BOOTS(637),
    DIAMOND_CHESTPLATE(635),
    DIAMOND_HELMET(634),
    DIAMOND_HOE(607),
    DIAMOND_HORSE_ARMOR(863),
    DIAMOND_LEGGINGS(636),
    DIAMOND_ORE(181),
    DIAMOND_PICKAXE(605),
    DIAMOND_SHOVEL(604),
    DIAMOND_SWORD(603),
    DIORITE(4),
    DIORITE_SLAB(555),
    DIORITE_STAIRS(542),
    DIORITE_WALL(300),
    DIRT(9),
    DISPENSER(80),
    DOLPHIN_SPAWN_EGG(768),
    DONKEY_SPAWN_EGG(769),
    DRAGON_BREATH(892),
    DRAGON_EGG(273),
    DRAGON_HEAD(841),
    DRIED_KELP(736),
    DRIED_KELP_BLOCK(676),
    DROPPER(330),
    DROWNED_SPAWN_EGG(770),
    EGG(682),
    ELDER_GUARDIAN_SPAWN_EGG(771),
    ELYTRA(898),
    EMERALD(827),
    EMERALD_BLOCK(279),
    EMERALD_ORE(276),
    ENCHANTED_BOOK(848),
    ENCHANTED_GOLDEN_APPLE(651),
    ENCHANTING_TABLE(269),
    END_CRYSTAL(886),
    END_PORTAL_FRAME(270),
    END_ROD(172),
    END_STONE(271),
    END_STONE_BRICK_SLAB(548),
    END_STONE_BRICK_STAIRS(534),
    END_STONE_BRICK_WALL(299),
    END_STONE_BRICKS(272),
    ENDER_CHEST(277),
    ENDER_EYE(757),
    ENDER_PEARL(744),
    ENDERMAN_SPAWN_EGG(772),
    ENDERMITE_SPAWN_EGG(773),
    EVOKER_SPAWN_EGG(774),
    EXPERIENCE_BOTTLE(823),
    FARMLAND(184),
    FEATHER(617),
    FERMENTED_SPIDER_EYE(752),
    FERN(90),
    FILLED_MAP(733),
    FIRE_CHARGE(824),
    FIRE_CORAL(510),
    FIRE_CORAL_BLOCK(505),
    FIRE_CORAL_FAN(520),
    FIREWORK_ROCKET(846),
    FIREWORK_STAR(847),
    FISHING_ROD(684),
    FLETCHING_TABLE(940),
    FLINT(646),
    FLINT_AND_STEEL(572),
    FLOWER_BANNER_PATTERN(929),
    FLOWER_POT(829),
    FOX_SPAWN_EGG(775),
    FURNACE(185),
    FURNACE_MINECART(681),
    GHAST_SPAWN_EGG(776),
    GHAST_TEAR(746),
    GILDED_BLACKSTONE(966),
    GLASS(77),
    GLASS_BOTTLE(750),
    GLASS_PANE(249),
    GLISTERING_MELON_SLICE(758),
    GLOBE_BANNER_PATTERN(933),
    GLOWSTONE(224),
    GLOWSTONE_DUST(686),
    GOLD_BLOCK(136),
    GOLD_INGOT(580),
    GOLD_NUGGET(747),
    GOLD_ORE(33),
    GOLDEN_APPLE(650),
    GOLDEN_AXE(596),
    GOLDEN_BOOTS(641),
    GOLDEN_CARROT(835),
    GOLDEN_CHESTPLATE(639),
    GOLDEN_HELMET(638),
    GOLDEN_HOE(597),
    GOLDEN_HORSE_ARMOR(862),
    GOLDEN_LEGGINGS(640),
    GOLDEN_PICKAXE(595),
    GOLDEN_SHOVEL(594),
    GOLDEN_SWORD(593),
    GRANITE(2),
    GRANITE_SLAB(551),
    GRANITE_STAIRS(538),
    GRANITE_WALL(293),
    GRASS(89),
    GRASS_BLOCK(8),
    GRASS_PATH(372),
    GRAVEL(32),
    GRAY_BANNER(877),
    GRAY_BED(723),
    GRAY_CARPET(357),
    GRAY_CONCRETE(471),
    GRAY_CONCRETE_POWDER(487),
    GRAY_DYE(703),
    GRAY_GLAZED_TERRACOTTA(455),
    GRAY_SHULKER_BOX(439),
    GRAY_STAINED_GLASS(386),
    GRAY_STAINED_GLASS_PANE(402),
    GRAY_TERRACOTTA(338),
    GRAY_WOOL(102),
    GREEN_BANNER(883),
    GREEN_BED(729),
    GREEN_CARPET(363),
    GREEN_CONCRETE(477),
    GREEN_CONCRETE_POWDER(493),
    GREEN_DYE(709),
    GREEN_GLAZED_TERRACOTTA(461),
    GREEN_SHULKER_BOX(445),
    GREEN_STAINED_GLASS(392),
    GREEN_STAINED_GLASS_PANE(408),
    GREEN_TERRACOTTA(344),
    GREEN_WOOL(108),
    GRINDSTONE(941),
    GUARDIAN_SPAWN_EGG(777),
    GUNPOWDER(618),
    HAY_BLOCK(349),
    HEART_OF_THE_SEA(925),
    HEAVY_WEIGHTED_PRESSURE_PLATE(319),
    HOGLIN_SPAWN_EGG(778),
    HONEY_BLOCK(956),
    HONEY_BOTTLE(955),
    HONEYCOMB(952),
    HONEYCOMB_BLOCK(957),
    HOPPER(323),
    HOPPER_MINECART(852),
    HORN_CORAL(511),
    HORN_CORAL_BLOCK(506),
    HORN_CORAL_FAN(521),
    HORSE_SPAWN_EGG(779),
    HUSK_SPAWN_EGG(780),
    ICE(203),
    INFESTED_CHISELED_STONE_BRICKS(239),
    INFESTED_COBBLESTONE(235),
    INFESTED_CRACKED_STONE_BRICKS(238),
    INFESTED_MOSSY_STONE_BRICKS(237),
    INFESTED_STONE(234),
    INFESTED_STONE_BRICKS(236),
    INK_SAC(693),
    IRON_AXE(601),
    IRON_BARS(247),
    IRON_BLOCK(137),
    IRON_BOOTS(633),
    IRON_CHESTPLATE(631),
    IRON_DOOR(557),
    IRON_HELMET(630),
    IRON_HOE(602),
    IRON_HORSE_ARMOR(861),
    IRON_INGOT(579),
    IRON_LEGGINGS(632),
    IRON_NUGGET(906),
    IRON_ORE(34),
    IRON_PICKAXE(600),
    IRON_SHOVEL(599),
    IRON_SWORD(598),
    IRON_TRAPDOOR(348),
    ITEM_FRAME(828),
    JACK_O_LANTERN(225),
    JIGSAW(569),
    JUKEBOX(207),
    JUNGLE_BOAT(901),
    JUNGLE_BUTTON(308),
    JUNGLE_DOOR(561),
    JUNGLE_FENCE(211),
    JUNGLE_FENCE_GATE(255),
    JUNGLE_LEAVES(72),
    JUNGLE_LOG(40),
    JUNGLE_PLANKS(18),
    JUNGLE_PRESSURE_PLATE(194),
    JUNGLE_SAPLING(26),
    JUNGLE_SIGN(655),
    JUNGLE_SLAB(141),
    JUNGLE_STAIRS(282),
    JUNGLE_TRAPDOOR(229),
    JUNGLE_WOOD(64),
    KELP(134),
    KNOWLEDGE_BOOK(907),
    LADDER(186),
    LANTERN(946),
    LAPIS_BLOCK(79),
    LAPIS_LAZULI(695),
    LAPIS_ORE(78),
    LARGE_FERN(378),
    LAVA_BUCKET(662),
    LEAD(865),
    LEATHER(668),
    LEATHER_BOOTS(625),
    LEATHER_CHESTPLATE(623),
    LEATHER_HELMET(622),
    LEATHER_HORSE_ARMOR(864),
    LEATHER_LEGGINGS(624),
    LECTERN(942),
    LEVER(189),
    LIGHT_BLUE_BANNER(873),
    LIGHT_BLUE_BED(719),
    LIGHT_BLUE_CARPET(353),
    LIGHT_BLUE_CONCRETE(467),
    LIGHT_BLUE_CONCRETE_POWDER(483),
    LIGHT_BLUE_DYE(699),
    LIGHT_BLUE_GLAZED_TERRACOTTA(451),
    LIGHT_BLUE_SHULKER_BOX(435),
    LIGHT_BLUE_STAINED_GLASS(382),
    LIGHT_BLUE_STAINED_GLASS_PANE(398),
    LIGHT_BLUE_TERRACOTTA(334),
    LIGHT_BLUE_WOOL(98),
    LIGHT_GRAY_BANNER(878),
    LIGHT_GRAY_BED(724),
    LIGHT_GRAY_CARPET(358),
    LIGHT_GRAY_CONCRETE(472),
    LIGHT_GRAY_CONCRETE_POWDER(488),
    LIGHT_GRAY_DYE(704),
    LIGHT_GRAY_GLAZED_TERRACOTTA(456),
    LIGHT_GRAY_SHULKER_BOX(440),
    LIGHT_GRAY_STAINED_GLASS(387),
    LIGHT_GRAY_STAINED_GLASS_PANE(403),
    LIGHT_GRAY_TERRACOTTA(339),
    LIGHT_GRAY_WOOL(103),
    LIGHT_WEIGHTED_PRESSURE_PLATE(318),
    LILAC(374),
    LILY_OF_THE_VALLEY(122),
    LILY_PAD(263),
    LIME_BANNER(875),
    LIME_BED(721),
    LIME_CARPET(355),
    LIME_CONCRETE(469),
    LIME_CONCRETE_POWDER(485),
    LIME_DYE(701),
    LIME_GLAZED_TERRACOTTA(453),
    LIME_SHULKER_BOX(437),
    LIME_STAINED_GLASS(384),
    LIME_STAINED_GLASS_PANE(400),
    LIME_TERRACOTTA(336),
    LIME_WOOL(100),
    LINGERING_POTION(896),
    LLAMA_SPAWN_EGG(781),
    LODESTONE(958),
    LOOM(928),
    MAGENTA_BANNER(872),
    MAGENTA_BED(718),
    MAGENTA_CARPET(352),
    MAGENTA_CONCRETE(466),
    MAGENTA_CONCRETE_POWDER(482),
    MAGENTA_DYE(698),
    MAGENTA_GLAZED_TERRACOTTA(450),
    MAGENTA_SHULKER_BOX(434),
    MAGENTA_STAINED_GLASS(381),
    MAGENTA_STAINED_GLASS_PANE(397),
    MAGENTA_TERRACOTTA(333),
    MAGENTA_WOOL(97),
    MAGMA_BLOCK(424),
    MAGMA_CREAM(754),
    MAGMA_CUBE_SPAWN_EGG(782),
    MAP(834),
    MELON(250),
    MELON_SEEDS(738),
    MELON_SLICE(735),
    MILK_BUCKET(669),
    MINECART(663),
    MOJANG_BANNER_PATTERN(932),
    MOOSHROOM_SPAWN_EGG(783),
    MOSSY_COBBLESTONE(169),
    MOSSY_COBBLESTONE_SLAB(547),
    MOSSY_COBBLESTONE_STAIRS(533),
    MOSSY_COBBLESTONE_WALL(288),
    MOSSY_STONE_BRICK_SLAB(545),
    MOSSY_STONE_BRICK_STAIRS(531),
    MOSSY_STONE_BRICK_WALL(292),
    MOSSY_STONE_BRICKS(241),
    MULE_SPAWN_EGG(784),
    MUSHROOM_STEM(246),
    MUSHROOM_STEW(615),
    MUSIC_DISC_11(919),
    MUSIC_DISC_13(909),
    MUSIC_DISC_BLOCKS(911),
    MUSIC_DISC_CAT(910),
    MUSIC_DISC_CHIRP(912),
    MUSIC_DISC_FAR(913),
    MUSIC_DISC_MALL(914),
    MUSIC_DISC_MELLOHI(915),
    MUSIC_DISC_PIGSTEP(921),
    MUSIC_DISC_STAL(916),
    MUSIC_DISC_STRAD(917),
    MUSIC_DISC_WAIT(920),
    MUSIC_DISC_WARD(918),
    MUTTON(868),
    MYCELIUM(262),
    NAME_TAG(866),
    NAUTILUS_SHELL(924),
    NETHER_BRICK(849),
    NETHER_BRICK_FENCE(267),
    NETHER_BRICK_SLAB(154),
    NETHER_BRICK_STAIRS(268),
    NETHER_BRICK_WALL(295),
    NETHER_BRICKS(264),
    NETHER_GOLD_ORE(36),
    NETHER_QUARTZ_ORE(322),
    NETHER_SPROUTS(130),
    NETHER_STAR(844),
    NETHER_WART(748),
    NETHER_WART_BLOCK(425),
    NETHERITE_AXE(611),
    NETHERITE_BLOCK(959),
    NETHERITE_BOOTS(645),
    NETHERITE_CHESTPLATE(643),
    NETHERITE_HELMET(642),
    NETHERITE_HOE(612),
    NETHERITE_INGOT(581),
    NETHERITE_LEGGINGS(644),
    NETHERITE_PICKAXE(610),
    NETHERITE_SCRAP(582),
    NETHERITE_SHOVEL(609),
    NETHERITE_SWORD(608),
    NETHERRACK(218),
    NOTE_BLOCK(84),
    OAK_BOAT(667),
    OAK_BUTTON(305),
    OAK_DOOR(558),
    OAK_FENCE(208),
    OAK_FENCE_GATE(252),
    OAK_LEAVES(69),
    OAK_LOG(37),
    OAK_PLANKS(15),
    OAK_PRESSURE_PLATE(191),
    OAK_SAPLING(23),
    OAK_SIGN(652),
    OAK_SLAB(138),
    OAK_STAIRS(179),
    OAK_TRAPDOOR(226),
    OAK_WOOD(61),
    OBSERVER(430),
    OBSIDIAN(170),
    OCELOT_SPAWN_EGG(785),
    ORANGE_BANNER(871),
    ORANGE_BED(717),
    ORANGE_CARPET(351),
    ORANGE_CONCRETE(465),
    ORANGE_CONCRETE_POWDER(481),
    ORANGE_DYE(697),
    ORANGE_GLAZED_TERRACOTTA(449),
    ORANGE_SHULKER_BOX(433),
    ORANGE_STAINED_GLASS(380),
    ORANGE_STAINED_GLASS_PANE(396),
    ORANGE_TERRACOTTA(332),
    ORANGE_TULIP(117),
    ORANGE_WOOL(96),
    OXEYE_DAISY(120),
    PACKED_ICE(368),
    PAINTING(649),
    PANDA_SPAWN_EGG(786),
    PAPER(677),
    PARROT_SPAWN_EGG(787),
    PEONY(376),
    PETRIFIED_OAK_SLAB(150),
    PHANTOM_MEMBRANE(923),
    PHANTOM_SPAWN_EGG(788),
    PIG_SPAWN_EGG(789),
    PIGLIN_BANNER_PATTERN(934),
    PIGLIN_BRUTE_SPAWN_EGG(791),
    PIGLIN_SPAWN_EGG(790),
    PILLAGER_SPAWN_EGG(792),
    PINK_BANNER(876),
    PINK_BED(722),
    PINK_CARPET(356),
    PINK_CONCRETE(470),
    PINK_CONCRETE_POWDER(486),
    PINK_DYE(702),
    PINK_GLAZED_TERRACOTTA(454),
    PINK_SHULKER_BOX(438),
    PINK_STAINED_GLASS(385),
    PINK_STAINED_GLASS_PANE(401),
    PINK_TERRACOTTA(337),
    PINK_TULIP(119),
    PINK_WOOL(101),
    PISTON(94),
    PLAYER_HEAD(838),
    PODZOL(11),
    POISONOUS_POTATO(833),
    POLAR_BEAR_SPAWN_EGG(793),
    POLISHED_ANDESITE(7),
    POLISHED_ANDESITE_SLAB(554),
    POLISHED_ANDESITE_STAIRS(541),
    POLISHED_BASALT(222),
    POLISHED_BLACKSTONE(967),
    POLISHED_BLACKSTONE_BRICK_SLAB(972),
    POLISHED_BLACKSTONE_BRICK_STAIRS(973),
    POLISHED_BLACKSTONE_BRICK_WALL(303),
    POLISHED_BLACKSTONE_BRICKS(971),
    POLISHED_BLACKSTONE_BUTTON(313),
    POLISHED_BLACKSTONE_PRESSURE_PLATE(199),
    POLISHED_BLACKSTONE_SLAB(968),
    POLISHED_BLACKSTONE_STAIRS(969),
    POLISHED_BLACKSTONE_WALL(302),
    POLISHED_DIORITE(5),
    POLISHED_DIORITE_SLAB(546),
    POLISHED_DIORITE_STAIRS(532),
    POLISHED_GRANITE(3),
    POLISHED_GRANITE_SLAB(543),
    POLISHED_GRANITE_STAIRS(529),
    POPPED_CHORUS_FRUIT(888),
    POPPY(112),
    PORKCHOP(647),
    POTATO(831),
    POTION(749),
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
    PUFFERFISH(690),
    PUFFERFISH_BUCKET(670),
    PUFFERFISH_SPAWN_EGG(794),
    PUMPKIN(216),
    PUMPKIN_PIE(845),
    PUMPKIN_SEEDS(737),
    PURPLE_BANNER(880),
    PURPLE_BED(726),
    PURPLE_CARPET(360),
    PURPLE_CONCRETE(474),
    PURPLE_CONCRETE_POWDER(490),
    PURPLE_DYE(706),
    PURPLE_GLAZED_TERRACOTTA(458),
    PURPLE_SHULKER_BOX(442),
    PURPLE_STAINED_GLASS(389),
    PURPLE_STAINED_GLASS_PANE(405),
    PURPLE_TERRACOTTA(341),
    PURPLE_WOOL(105),
    PURPUR_BLOCK(175),
    PURPUR_PILLAR(176),
    PURPUR_SLAB(158),
    PURPUR_STAIRS(177),
    QUARTZ(850),
    QUARTZ_BLOCK(325),
    QUARTZ_BRICKS(326),
    QUARTZ_PILLAR(327),
    QUARTZ_SLAB(155),
    QUARTZ_STAIRS(328),
    RABBIT(855),
    RABBIT_FOOT(858),
    RABBIT_HIDE(859),
    RABBIT_SPAWN_EGG(795),
    RABBIT_STEW(857),
    RAIL(187),
    RAVAGER_SPAWN_EGG(796),
    RED_BANNER(884),
    RED_BED(730),
    RED_CARPET(364),
    RED_CONCRETE(478),
    RED_CONCRETE_POWDER(494),
    RED_DYE(710),
    RED_GLAZED_TERRACOTTA(462),
    RED_MUSHROOM(125),
    RED_MUSHROOM_BLOCK(245),
    RED_NETHER_BRICK_SLAB(553),
    RED_NETHER_BRICK_STAIRS(540),
    RED_NETHER_BRICK_WALL(297),
    RED_NETHER_BRICKS(427),
    RED_SAND(31),
    RED_SANDSTONE(418),
    RED_SANDSTONE_SLAB(156),
    RED_SANDSTONE_STAIRS(421),
    RED_SANDSTONE_WALL(291),
    RED_SHULKER_BOX(446),
    RED_STAINED_GLASS(393),
    RED_STAINED_GLASS_PANE(409),
    RED_TERRACOTTA(345),
    RED_TULIP(116),
    RED_WOOL(109),
    REDSTONE(665),
    REDSTONE_BLOCK(321),
    REDSTONE_LAMP(274),
    REDSTONE_ORE(200),
    REDSTONE_TORCH(201),
    REPEATER(566),
    REPEATING_COMMAND_BLOCK(422),
    RESPAWN_ANCHOR(975),
    ROSE_BUSH(375),
    ROTTEN_FLESH(743),
    SADDLE(664),
    SALMON(688),
    SALMON_BUCKET(671),
    SALMON_SPAWN_EGG(797),
    SAND(30),
    SANDSTONE(81),
    SANDSTONE_SLAB(148),
    SANDSTONE_STAIRS(275),
    SANDSTONE_WALL(298),
    SCAFFOLDING(556),
    SCUTE(571),
    SEA_LANTERN(417),
    SEA_PICKLE(93),
    SEAGRASS(92),
    SHEARS(734),
    SHEEP_SPAWN_EGG(798),
    SHIELD(897),
    SHROOMLIGHT(951),
    SHULKER_BOX(431),
    SHULKER_SHELL(905),
    SHULKER_SPAWN_EGG(799),
    SILVERFISH_SPAWN_EGG(800),
    SKELETON_HORSE_SPAWN_EGG(802),
    SKELETON_SKULL(836),
    SKELETON_SPAWN_EGG(801),
    SKULL_BANNER_PATTERN(931),
    SLIME_BALL(679),
    SLIME_BLOCK(371),
    SLIME_SPAWN_EGG(803),
    SMITHING_TABLE(943),
    SMOKER(937),
    SMOOTH_QUARTZ(162),
    SMOOTH_QUARTZ_SLAB(550),
    SMOOTH_QUARTZ_STAIRS(537),
    SMOOTH_RED_SANDSTONE(163),
    SMOOTH_RED_SANDSTONE_SLAB(544),
    SMOOTH_RED_SANDSTONE_STAIRS(530),
    SMOOTH_SANDSTONE(164),
    SMOOTH_SANDSTONE_SLAB(549),
    SMOOTH_SANDSTONE_STAIRS(536),
    SMOOTH_STONE(165),
    SMOOTH_STONE_SLAB(147),
    SNOW(202),
    SNOW_BLOCK(204),
    SNOWBALL(666),
    SOUL_CAMPFIRE(950),
    SOUL_LANTERN(947),
    SOUL_SAND(219),
    SOUL_SOIL(220),
    SOUL_TORCH(223),
    SPAWNER(178),
    SPECTRAL_ARROW(894),
    SPIDER_EYE(751),
    SPIDER_SPAWN_EGG(804),
    SPLASH_POTION(893),
    SPONGE(75),
    SPRUCE_BOAT(899),
    SPRUCE_BUTTON(306),
    SPRUCE_DOOR(559),
    SPRUCE_FENCE(209),
    SPRUCE_FENCE_GATE(253),
    SPRUCE_LEAVES(70),
    SPRUCE_LOG(38),
    SPRUCE_PLANKS(16),
    SPRUCE_PRESSURE_PLATE(192),
    SPRUCE_SAPLING(24),
    SPRUCE_SIGN(653),
    SPRUCE_SLAB(139),
    SPRUCE_STAIRS(280),
    SPRUCE_TRAPDOOR(227),
    SPRUCE_WOOD(62),
    SQUID_SPAWN_EGG(805),
    STICK(613),
    STICKY_PISTON(87),
    STONE(1),
    STONE_AXE(591),
    STONE_BRICK_SLAB(153),
    STONE_BRICK_STAIRS(261),
    STONE_BRICK_WALL(294),
    STONE_BRICKS(240),
    STONE_BUTTON(304),
    STONE_HOE(592),
    STONE_PICKAXE(590),
    STONE_PRESSURE_PLATE(190),
    STONE_SHOVEL(589),
    STONE_SLAB(146),
    STONE_STAIRS(535),
    STONE_SWORD(588),
    STONECUTTER(944),
    STRAY_SPAWN_EGG(806),
    STRIDER_SPAWN_EGG(807),
    STRING(616),
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
    SUGAR(714),
    SUGAR_CANE(133),
    SUNFLOWER(373),
    SUSPICIOUS_STEW(927),
    SWEET_BERRIES(948),
    TALL_GRASS(377),
    TARGET(961),
    TERRACOTTA(366),
    TIPPED_ARROW(895),
    TNT(167),
    TNT_MINECART(851),
    TORCH(171),
    TOTEM_OF_UNDYING(904),
    TRADER_LLAMA_SPAWN_EGG(808),
    TRAPPED_CHEST(317),
    TRIDENT(922),
    TRIPWIRE_HOOK(278),
    TROPICAL_FISH(689),
    TROPICAL_FISH_BUCKET(673),
    TROPICAL_FISH_SPAWN_EGG(809),
    TUBE_CORAL(507),
    TUBE_CORAL_BLOCK(502),
    TUBE_CORAL_FAN(517),
    TURTLE_EGG(496),
    TURTLE_HELMET(570),
    TURTLE_SPAWN_EGG(810),
    TWISTING_VINES(132),
    VEX_SPAWN_EGG(811),
    VILLAGER_SPAWN_EGG(812),
    VINDICATOR_SPAWN_EGG(813),
    VINE(251),
    WANDERING_TRADER_SPAWN_EGG(814),
    WARPED_BUTTON(312),
    WARPED_DOOR(565),
    WARPED_FENCE(215),
    WARPED_FENCE_GATE(259),
    WARPED_FUNGUS(127),
    WARPED_FUNGUS_ON_A_STICK(843),
    WARPED_HYPHAE(68),
    WARPED_NYLIUM(13),
    WARPED_PLANKS(22),
    WARPED_PRESSURE_PLATE(198),
    WARPED_ROOTS(129),
    WARPED_SIGN(659),
    WARPED_SLAB(145),
    WARPED_STAIRS(284),
    WARPED_STEM(44),
    WARPED_TRAPDOOR(233),
    WARPED_WART_BLOCK(426),
    WATER_BUCKET(661),
    WEEPING_VINES(131),
    WET_SPONGE(76),
    WHEAT(620),
    WHEAT_SEEDS(619),
    WHITE_BANNER(870),
    WHITE_BED(716),
    WHITE_CARPET(350),
    WHITE_CONCRETE(464),
    WHITE_CONCRETE_POWDER(480),
    WHITE_DYE(696),
    WHITE_GLAZED_TERRACOTTA(448),
    WHITE_SHULKER_BOX(432),
    WHITE_STAINED_GLASS(379),
    WHITE_STAINED_GLASS_PANE(395),
    WHITE_TERRACOTTA(331),
    WHITE_TULIP(118),
    WHITE_WOOL(95),
    WITCH_SPAWN_EGG(815),
    WITHER_ROSE(123),
    WITHER_SKELETON_SKULL(837),
    WITHER_SKELETON_SPAWN_EGG(816),
    WOLF_SPAWN_EGG(817),
    WOODEN_AXE(586),
    WOODEN_HOE(587),
    WOODEN_PICKAXE(585),
    WOODEN_SHOVEL(584),
    WOODEN_SWORD(583),
    WRITABLE_BOOK(825),
    WRITTEN_BOOK(826),
    YELLOW_BANNER(874),
    YELLOW_BED(720),
    YELLOW_CARPET(354),
    YELLOW_CONCRETE(468),
    YELLOW_CONCRETE_POWDER(484),
    YELLOW_DYE(700),
    YELLOW_GLAZED_TERRACOTTA(452),
    YELLOW_SHULKER_BOX(436),
    YELLOW_STAINED_GLASS(383),
    YELLOW_STAINED_GLASS_PANE(399),
    YELLOW_TERRACOTTA(335),
    YELLOW_WOOL(99),
    ZOGLIN_SPAWN_EGG(818),
    ZOMBIE_HEAD(839),
    ZOMBIE_HORSE_SPAWN_EGG(820),
    ZOMBIE_SPAWN_EGG(819),
    ZOMBIE_VILLAGER_SPAWN_EGG(821),
    ZOMBIFIED_PIGLIN_SPAWN_EGG(822);

    companion object {
        fun getItemFromID(id: Int): Item? = Item.entries.find {
            it.id == id
        }

        /**
         * Converts a global palette item ID to a state palette item ID
         *
         * @param item The global block palette to get the state ID of
         * @param properties Any optional properties to match against the item's states, for example the
         * grass block has two properties, "snowy" and "default".
         *
         * @return The state ID of the state palette related to the block
         */
        fun getStateID(item: Item, properties: Map<String, String>? = null): Int {
            val jsonStream = Item::class.java.getResourceAsStream("/blocks.json")
                ?: throw IllegalArgumentException("blocks.json not found")

            val reader = InputStreamReader(jsonStream)
            val json = JsonParser.parseReader(reader).asJsonObject
            val blockKey = "minecraft:${item.name.lowercase()}"

            val itemData = json[blockKey]?.asJsonObject
                ?: throw IllegalArgumentException("Block $blockKey not found")
            val states = itemData["states"]?.asJsonArray
                ?: throw IllegalArgumentException("Block $blockKey has no states")

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

package com.aznos.world

fun Block.GRASS_BLOCK(snowy: Boolean = false): BlockInstance {
    require(this == Block.GRASS_BLOCK) { "Not a GRASS_BLOCK block" }
    val stateId = if (snowy) 8 else 9
    return BlockInstance(this, stateId, mapOf("snowy" to snowy))
}

fun Block.PODZOL(snowy: Boolean = false): BlockInstance {
    require(this == Block.PODZOL) { "Not a PODZOL block" }
    val stateId = if (snowy) 12 else 13
    return BlockInstance(this, stateId, mapOf("snowy" to snowy))
}

            fun Block.OAK_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.OAK_SAPLING) { "Not a OAK_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 21
"1" -> 22
                else -> 21
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.SPRUCE_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.SPRUCE_SAPLING) { "Not a SPRUCE_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 23
"1" -> 24
                else -> 23
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.BIRCH_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.BIRCH_SAPLING) { "Not a BIRCH_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 25
"1" -> 26
                else -> 25
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.JUNGLE_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.JUNGLE_SAPLING) { "Not a JUNGLE_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 27
"1" -> 28
                else -> 27
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.ACACIA_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.ACACIA_SAPLING) { "Not a ACACIA_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 29
"1" -> 30
                else -> 29
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.DARK_OAK_SAPLING(stage: Int = 0): BlockInstance {
                require(this == Block.DARK_OAK_SAPLING) { "Not a DARK_OAK_SAPLING block" }
                            val stateId = when(stage.toString()) {
"0" -> 31
"1" -> 32
                else -> 31
            }
                return BlockInstance(this, stateId, mapOf("stage" to stage))
            }

            fun Block.WATER(level: Int = 0): BlockInstance {
                require(this == Block.WATER) { "Not a WATER block" }
                            val stateId = when(level.toString()) {
"0" -> 34
"1" -> 35
"2" -> 36
"3" -> 37
"4" -> 38
"5" -> 39
"6" -> 40
"7" -> 41
"8" -> 42
"9" -> 43
"10" -> 44
"11" -> 45
"12" -> 46
"13" -> 47
"14" -> 48
"15" -> 49
                else -> 34
            }
                return BlockInstance(this, stateId, mapOf("level" to level))
            }

            fun Block.LAVA(level: Int = 0): BlockInstance {
                require(this == Block.LAVA) { "Not a LAVA block" }
                            val stateId = when(level.toString()) {
"0" -> 50
"1" -> 51
"2" -> 52
"3" -> 53
"4" -> 54
"5" -> 55
"6" -> 56
"7" -> 57
"8" -> 58
"9" -> 59
"10" -> 60
"11" -> 61
"12" -> 62
"13" -> 63
"14" -> 64
"15" -> 65
                else -> 50
            }
                return BlockInstance(this, stateId, mapOf("level" to level))
            }

            fun Block.OAK_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.OAK_LOG) { "Not a OAK_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 73
"y" -> 74
"z" -> 75
                else -> 74
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.SPRUCE_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.SPRUCE_LOG) { "Not a SPRUCE_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 76
"y" -> 77
"z" -> 78
                else -> 77
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.BIRCH_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.BIRCH_LOG) { "Not a BIRCH_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 79
"y" -> 80
"z" -> 81
                else -> 80
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.JUNGLE_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.JUNGLE_LOG) { "Not a JUNGLE_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 82
"y" -> 83
"z" -> 84
                else -> 83
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.ACACIA_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.ACACIA_LOG) { "Not a ACACIA_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 85
"y" -> 86
"z" -> 87
                else -> 86
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.DARK_OAK_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.DARK_OAK_LOG) { "Not a DARK_OAK_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 88
"y" -> 89
"z" -> 90
                else -> 89
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_SPRUCE_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_SPRUCE_LOG) { "Not a STRIPPED_SPRUCE_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 91
"y" -> 92
"z" -> 93
                else -> 92
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_BIRCH_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_BIRCH_LOG) { "Not a STRIPPED_BIRCH_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 94
"y" -> 95
"z" -> 96
                else -> 95
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_JUNGLE_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_JUNGLE_LOG) { "Not a STRIPPED_JUNGLE_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 97
"y" -> 98
"z" -> 99
                else -> 98
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_ACACIA_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_ACACIA_LOG) { "Not a STRIPPED_ACACIA_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 100
"y" -> 101
"z" -> 102
                else -> 101
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_DARK_OAK_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_DARK_OAK_LOG) { "Not a STRIPPED_DARK_OAK_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 103
"y" -> 104
"z" -> 105
                else -> 104
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_OAK_LOG(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_OAK_LOG) { "Not a STRIPPED_OAK_LOG block" }
                            val stateId = when(axis.toString()) {
"x" -> 106
"y" -> 107
"z" -> 108
                else -> 107
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.OAK_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.OAK_WOOD) { "Not a OAK_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 109
"y" -> 110
"z" -> 111
                else -> 110
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.SPRUCE_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.SPRUCE_WOOD) { "Not a SPRUCE_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 112
"y" -> 113
"z" -> 114
                else -> 113
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.BIRCH_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.BIRCH_WOOD) { "Not a BIRCH_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 115
"y" -> 116
"z" -> 117
                else -> 116
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.JUNGLE_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.JUNGLE_WOOD) { "Not a JUNGLE_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 118
"y" -> 119
"z" -> 120
                else -> 119
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.ACACIA_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.ACACIA_WOOD) { "Not a ACACIA_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 121
"y" -> 122
"z" -> 123
                else -> 122
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.DARK_OAK_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.DARK_OAK_WOOD) { "Not a DARK_OAK_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 124
"y" -> 125
"z" -> 126
                else -> 125
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_OAK_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_OAK_WOOD) { "Not a STRIPPED_OAK_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 127
"y" -> 128
"z" -> 129
                else -> 128
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_SPRUCE_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_SPRUCE_WOOD) { "Not a STRIPPED_SPRUCE_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 130
"y" -> 131
"z" -> 132
                else -> 131
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_BIRCH_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_BIRCH_WOOD) { "Not a STRIPPED_BIRCH_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 133
"y" -> 134
"z" -> 135
                else -> 134
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_JUNGLE_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_JUNGLE_WOOD) { "Not a STRIPPED_JUNGLE_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 136
"y" -> 137
"z" -> 138
                else -> 137
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_ACACIA_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_ACACIA_WOOD) { "Not a STRIPPED_ACACIA_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 139
"y" -> 140
"z" -> 141
                else -> 140
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_DARK_OAK_WOOD(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_DARK_OAK_WOOD) { "Not a STRIPPED_DARK_OAK_WOOD block" }
                            val stateId = when(axis.toString()) {
"x" -> 142
"y" -> 143
"z" -> 144
                else -> 143
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.TALL_SEAGRASS(half: String = "lower"): BlockInstance {
                require(this == Block.TALL_SEAGRASS) { "Not a TALL_SEAGRASS block" }
                            val stateId = when(half.toString()) {
"upper" -> 1346
"lower" -> 1347
                else -> 1347
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

fun Block.TNT(unstable: Boolean = false): BlockInstance {
    require(this == Block.TNT) { "Not a TNT block" }
    val stateId = if (unstable) 1430 else 1431
    return BlockInstance(this, stateId, mapOf("unstable" to unstable))
}

            fun Block.WALL_TORCH(facing: String = "north"): BlockInstance {
                require(this == Block.WALL_TORCH) { "Not a WALL_TORCH block" }
                            val stateId = when(facing.toString()) {
"north" -> 1436
"south" -> 1437
"west" -> 1438
"east" -> 1439
                else -> 1436
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.WHEAT(age: Int = 0): BlockInstance {
                require(this == Block.WHEAT) { "Not a WHEAT block" }
                            val stateId = when(age.toString()) {
"0" -> 3357
"1" -> 3358
"2" -> 3359
"3" -> 3360
"4" -> 3361
"5" -> 3362
"6" -> 3363
"7" -> 3364
                else -> 3357
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.FARMLAND(moisture: Int = 0): BlockInstance {
                require(this == Block.FARMLAND) { "Not a FARMLAND block" }
                            val stateId = when(moisture.toString()) {
"0" -> 3365
"1" -> 3366
"2" -> 3367
"3" -> 3368
"4" -> 3369
"5" -> 3370
"6" -> 3371
"7" -> 3372
                else -> 3365
            }
                return BlockInstance(this, stateId, mapOf("moisture" to moisture))
            }

            fun Block.RAIL(shape: String = "north_south"): BlockInstance {
                require(this == Block.RAIL) { "Not a RAIL block" }
                            val stateId = when(shape.toString()) {
"north_south" -> 3645
"east_west" -> 3646
"ascending_east" -> 3647
"ascending_west" -> 3648
"ascending_north" -> 3649
"ascending_south" -> 3650
"south_east" -> 3651
"south_west" -> 3652
"north_west" -> 3653
"north_east" -> 3654
                else -> 3645
            }
                return BlockInstance(this, stateId, mapOf("shape" to shape))
            }

fun Block.STONE_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.STONE_PRESSURE_PLATE) { "Not a STONE_PRESSURE_PLATE block" }
    val stateId = if (powered) 3807 else 3808
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.OAK_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.OAK_PRESSURE_PLATE) { "Not a OAK_PRESSURE_PLATE block" }
    val stateId = if (powered) 3873 else 3874
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.SPRUCE_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.SPRUCE_PRESSURE_PLATE) { "Not a SPRUCE_PRESSURE_PLATE block" }
    val stateId = if (powered) 3875 else 3876
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.BIRCH_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.BIRCH_PRESSURE_PLATE) { "Not a BIRCH_PRESSURE_PLATE block" }
    val stateId = if (powered) 3877 else 3878
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.JUNGLE_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.JUNGLE_PRESSURE_PLATE) { "Not a JUNGLE_PRESSURE_PLATE block" }
    val stateId = if (powered) 3879 else 3880
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.ACACIA_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.ACACIA_PRESSURE_PLATE) { "Not a ACACIA_PRESSURE_PLATE block" }
    val stateId = if (powered) 3881 else 3882
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.DARK_OAK_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.DARK_OAK_PRESSURE_PLATE) { "Not a DARK_OAK_PRESSURE_PLATE block" }
    val stateId = if (powered) 3883 else 3884
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.REDSTONE_ORE(lit: Boolean = false): BlockInstance {
    require(this == Block.REDSTONE_ORE) { "Not a REDSTONE_ORE block" }
    val stateId = if (lit) 3885 else 3886
    return BlockInstance(this, stateId, mapOf("lit" to lit))
}

fun Block.REDSTONE_TORCH(lit: Boolean = true): BlockInstance {
    require(this == Block.REDSTONE_TORCH) { "Not a REDSTONE_TORCH block" }
    val stateId = if (lit) 3887 else 3888
    return BlockInstance(this, stateId, mapOf("lit" to lit))
}

            fun Block.SNOW(layers: Int = 1): BlockInstance {
                require(this == Block.SNOW) { "Not a SNOW block" }
                            val stateId = when(layers.toString()) {
"1" -> 3921
"2" -> 3922
"3" -> 3923
"4" -> 3924
"5" -> 3925
"6" -> 3926
"7" -> 3927
"8" -> 3928
                else -> 3921
            }
                return BlockInstance(this, stateId, mapOf("layers" to layers))
            }

            fun Block.CACTUS(age: Int = 0): BlockInstance {
                require(this == Block.CACTUS) { "Not a CACTUS block" }
                            val stateId = when(age.toString()) {
"0" -> 3931
"1" -> 3932
"2" -> 3933
"3" -> 3934
"4" -> 3935
"5" -> 3936
"6" -> 3937
"7" -> 3938
"8" -> 3939
"9" -> 3940
"10" -> 3941
"11" -> 3942
"12" -> 3943
"13" -> 3944
"14" -> 3945
"15" -> 3946
                else -> 3931
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.SUGAR_CANE(age: Int = 0): BlockInstance {
                require(this == Block.SUGAR_CANE) { "Not a SUGAR_CANE block" }
                            val stateId = when(age.toString()) {
"0" -> 3948
"1" -> 3949
"2" -> 3950
"3" -> 3951
"4" -> 3952
"5" -> 3953
"6" -> 3954
"7" -> 3955
"8" -> 3956
"9" -> 3957
"10" -> 3958
"11" -> 3959
"12" -> 3960
"13" -> 3961
"14" -> 3962
"15" -> 3963
                else -> 3948
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

fun Block.JUKEBOX(has_record: Boolean = false): BlockInstance {
    require(this == Block.JUKEBOX) { "Not a JUKEBOX block" }
    val stateId = if (has_record) 3964 else 3965
    return BlockInstance(this, stateId, mapOf("has_record" to has_record))
}

            fun Block.BASALT(axis: String = "y"): BlockInstance {
                require(this == Block.BASALT) { "Not a BASALT block" }
                            val stateId = when(axis.toString()) {
"x" -> 4002
"y" -> 4003
"z" -> 4004
                else -> 4003
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.POLISHED_BASALT(axis: String = "y"): BlockInstance {
                require(this == Block.POLISHED_BASALT) { "Not a POLISHED_BASALT block" }
                            val stateId = when(axis.toString()) {
"x" -> 4005
"y" -> 4006
"z" -> 4007
                else -> 4006
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.SOUL_WALL_TORCH(facing: String = "north"): BlockInstance {
                require(this == Block.SOUL_WALL_TORCH) { "Not a SOUL_WALL_TORCH block" }
                            val stateId = when(facing.toString()) {
"north" -> 4009
"south" -> 4010
"west" -> 4011
"east" -> 4012
                else -> 4009
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.NETHER_PORTAL(axis: String = "x"): BlockInstance {
                require(this == Block.NETHER_PORTAL) { "Not a NETHER_PORTAL block" }
                            val stateId = when(axis.toString()) {
"x" -> 4014
"z" -> 4015
                else -> 4014
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.CARVED_PUMPKIN(facing: String = "north"): BlockInstance {
                require(this == Block.CARVED_PUMPKIN) { "Not a CARVED_PUMPKIN block" }
                            val stateId = when(facing.toString()) {
"north" -> 4016
"south" -> 4017
"west" -> 4018
"east" -> 4019
                else -> 4016
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.JACK_O_LANTERN(facing: String = "north"): BlockInstance {
                require(this == Block.JACK_O_LANTERN) { "Not a JACK_O_LANTERN block" }
                            val stateId = when(facing.toString()) {
"north" -> 4020
"south" -> 4021
"west" -> 4022
"east" -> 4023
                else -> 4020
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CAKE(bites: Int = 0): BlockInstance {
                require(this == Block.CAKE) { "Not a CAKE block" }
                            val stateId = when(bites.toString()) {
"0" -> 4024
"1" -> 4025
"2" -> 4026
"3" -> 4027
"4" -> 4028
"5" -> 4029
"6" -> 4030
                else -> 4024
            }
                return BlockInstance(this, stateId, mapOf("bites" to bites))
            }

            fun Block.ATTACHED_PUMPKIN_STEM(facing: String = "north"): BlockInstance {
                require(this == Block.ATTACHED_PUMPKIN_STEM) { "Not a ATTACHED_PUMPKIN_STEM block" }
                            val stateId = when(facing.toString()) {
"north" -> 4768
"south" -> 4769
"west" -> 4770
"east" -> 4771
                else -> 4768
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ATTACHED_MELON_STEM(facing: String = "north"): BlockInstance {
                require(this == Block.ATTACHED_MELON_STEM) { "Not a ATTACHED_MELON_STEM block" }
                            val stateId = when(facing.toString()) {
"north" -> 4772
"south" -> 4773
"west" -> 4774
"east" -> 4775
                else -> 4772
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PUMPKIN_STEM(age: Int = 0): BlockInstance {
                require(this == Block.PUMPKIN_STEM) { "Not a PUMPKIN_STEM block" }
                            val stateId = when(age.toString()) {
"0" -> 4776
"1" -> 4777
"2" -> 4778
"3" -> 4779
"4" -> 4780
"5" -> 4781
"6" -> 4782
"7" -> 4783
                else -> 4776
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.MELON_STEM(age: Int = 0): BlockInstance {
                require(this == Block.MELON_STEM) { "Not a MELON_STEM block" }
                            val stateId = when(age.toString()) {
"0" -> 4784
"1" -> 4785
"2" -> 4786
"3" -> 4787
"4" -> 4788
"5" -> 4789
"6" -> 4790
"7" -> 4791
                else -> 4784
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

fun Block.MYCELIUM(snowy: Boolean = false): BlockInstance {
    require(this == Block.MYCELIUM) { "Not a MYCELIUM block" }
    val stateId = if (snowy) 5016 else 5017
    return BlockInstance(this, stateId, mapOf("snowy" to snowy))
}

            fun Block.NETHER_WART(age: Int = 0): BlockInstance {
                require(this == Block.NETHER_WART) { "Not a NETHER_WART block" }
                            val stateId = when(age.toString()) {
"0" -> 5132
"1" -> 5133
"2" -> 5134
"3" -> 5135
                else -> 5132
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.CAULDRON(level: Int = 0): BlockInstance {
                require(this == Block.CAULDRON) { "Not a CAULDRON block" }
                            val stateId = when(level.toString()) {
"0" -> 5145
"1" -> 5146
"2" -> 5147
"3" -> 5148
                else -> 5145
            }
                return BlockInstance(this, stateId, mapOf("level" to level))
            }

fun Block.REDSTONE_LAMP(lit: Boolean = false): BlockInstance {
    require(this == Block.REDSTONE_LAMP) { "Not a REDSTONE_LAMP block" }
    val stateId = if (lit) 5160 else 5161
    return BlockInstance(this, stateId, mapOf("lit" to lit))
}

            fun Block.CARROTS(age: Int = 0): BlockInstance {
                require(this == Block.CARROTS) { "Not a CARROTS block" }
                            val stateId = when(age.toString()) {
"0" -> 6334
"1" -> 6335
"2" -> 6336
"3" -> 6337
"4" -> 6338
"5" -> 6339
"6" -> 6340
"7" -> 6341
                else -> 6334
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.POTATOES(age: Int = 0): BlockInstance {
                require(this == Block.POTATOES) { "Not a POTATOES block" }
                            val stateId = when(age.toString()) {
"0" -> 6342
"1" -> 6343
"2" -> 6344
"3" -> 6345
"4" -> 6346
"5" -> 6347
"6" -> 6348
"7" -> 6349
                else -> 6342
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.SKELETON_SKULL(rotation: Int = 0): BlockInstance {
                require(this == Block.SKELETON_SKULL) { "Not a SKELETON_SKULL block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6494
"1" -> 6495
"2" -> 6496
"3" -> 6497
"4" -> 6498
"5" -> 6499
"6" -> 6500
"7" -> 6501
"8" -> 6502
"9" -> 6503
"10" -> 6504
"11" -> 6505
"12" -> 6506
"13" -> 6507
"14" -> 6508
"15" -> 6509
                else -> 6494
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.SKELETON_WALL_SKULL(facing: String = "north"): BlockInstance {
                require(this == Block.SKELETON_WALL_SKULL) { "Not a SKELETON_WALL_SKULL block" }
                            val stateId = when(facing.toString()) {
"north" -> 6510
"south" -> 6511
"west" -> 6512
"east" -> 6513
                else -> 6510
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.WITHER_SKELETON_SKULL(rotation: Int = 0): BlockInstance {
                require(this == Block.WITHER_SKELETON_SKULL) { "Not a WITHER_SKELETON_SKULL block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6514
"1" -> 6515
"2" -> 6516
"3" -> 6517
"4" -> 6518
"5" -> 6519
"6" -> 6520
"7" -> 6521
"8" -> 6522
"9" -> 6523
"10" -> 6524
"11" -> 6525
"12" -> 6526
"13" -> 6527
"14" -> 6528
"15" -> 6529
                else -> 6514
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.WITHER_SKELETON_WALL_SKULL(facing: String = "north"): BlockInstance {
                require(this == Block.WITHER_SKELETON_WALL_SKULL) { "Not a WITHER_SKELETON_WALL_SKULL block" }
                            val stateId = when(facing.toString()) {
"north" -> 6530
"south" -> 6531
"west" -> 6532
"east" -> 6533
                else -> 6530
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ZOMBIE_HEAD(rotation: Int = 0): BlockInstance {
                require(this == Block.ZOMBIE_HEAD) { "Not a ZOMBIE_HEAD block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6534
"1" -> 6535
"2" -> 6536
"3" -> 6537
"4" -> 6538
"5" -> 6539
"6" -> 6540
"7" -> 6541
"8" -> 6542
"9" -> 6543
"10" -> 6544
"11" -> 6545
"12" -> 6546
"13" -> 6547
"14" -> 6548
"15" -> 6549
                else -> 6534
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.ZOMBIE_WALL_HEAD(facing: String = "north"): BlockInstance {
                require(this == Block.ZOMBIE_WALL_HEAD) { "Not a ZOMBIE_WALL_HEAD block" }
                            val stateId = when(facing.toString()) {
"north" -> 6550
"south" -> 6551
"west" -> 6552
"east" -> 6553
                else -> 6550
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PLAYER_HEAD(rotation: Int = 0): BlockInstance {
                require(this == Block.PLAYER_HEAD) { "Not a PLAYER_HEAD block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6554
"1" -> 6555
"2" -> 6556
"3" -> 6557
"4" -> 6558
"5" -> 6559
"6" -> 6560
"7" -> 6561
"8" -> 6562
"9" -> 6563
"10" -> 6564
"11" -> 6565
"12" -> 6566
"13" -> 6567
"14" -> 6568
"15" -> 6569
                else -> 6554
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.PLAYER_WALL_HEAD(facing: String = "north"): BlockInstance {
                require(this == Block.PLAYER_WALL_HEAD) { "Not a PLAYER_WALL_HEAD block" }
                            val stateId = when(facing.toString()) {
"north" -> 6570
"south" -> 6571
"west" -> 6572
"east" -> 6573
                else -> 6570
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CREEPER_HEAD(rotation: Int = 0): BlockInstance {
                require(this == Block.CREEPER_HEAD) { "Not a CREEPER_HEAD block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6574
"1" -> 6575
"2" -> 6576
"3" -> 6577
"4" -> 6578
"5" -> 6579
"6" -> 6580
"7" -> 6581
"8" -> 6582
"9" -> 6583
"10" -> 6584
"11" -> 6585
"12" -> 6586
"13" -> 6587
"14" -> 6588
"15" -> 6589
                else -> 6574
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.CREEPER_WALL_HEAD(facing: String = "north"): BlockInstance {
                require(this == Block.CREEPER_WALL_HEAD) { "Not a CREEPER_WALL_HEAD block" }
                            val stateId = when(facing.toString()) {
"north" -> 6590
"south" -> 6591
"west" -> 6592
"east" -> 6593
                else -> 6590
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.DRAGON_HEAD(rotation: Int = 0): BlockInstance {
                require(this == Block.DRAGON_HEAD) { "Not a DRAGON_HEAD block" }
                            val stateId = when(rotation.toString()) {
"0" -> 6594
"1" -> 6595
"2" -> 6596
"3" -> 6597
"4" -> 6598
"5" -> 6599
"6" -> 6600
"7" -> 6601
"8" -> 6602
"9" -> 6603
"10" -> 6604
"11" -> 6605
"12" -> 6606
"13" -> 6607
"14" -> 6608
"15" -> 6609
                else -> 6594
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.DRAGON_WALL_HEAD(facing: String = "north"): BlockInstance {
                require(this == Block.DRAGON_WALL_HEAD) { "Not a DRAGON_WALL_HEAD block" }
                            val stateId = when(facing.toString()) {
"north" -> 6610
"south" -> 6611
"west" -> 6612
"east" -> 6613
                else -> 6610
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ANVIL(facing: String = "north"): BlockInstance {
                require(this == Block.ANVIL) { "Not a ANVIL block" }
                            val stateId = when(facing.toString()) {
"north" -> 6614
"south" -> 6615
"west" -> 6616
"east" -> 6617
                else -> 6614
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CHIPPED_ANVIL(facing: String = "north"): BlockInstance {
                require(this == Block.CHIPPED_ANVIL) { "Not a CHIPPED_ANVIL block" }
                            val stateId = when(facing.toString()) {
"north" -> 6618
"south" -> 6619
"west" -> 6620
"east" -> 6621
                else -> 6618
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.DAMAGED_ANVIL(facing: String = "north"): BlockInstance {
                require(this == Block.DAMAGED_ANVIL) { "Not a DAMAGED_ANVIL block" }
                            val stateId = when(facing.toString()) {
"north" -> 6622
"south" -> 6623
"west" -> 6624
"east" -> 6625
                else -> 6622
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_WEIGHTED_PRESSURE_PLATE(power: Int = 0): BlockInstance {
                require(this == Block.LIGHT_WEIGHTED_PRESSURE_PLATE) { "Not a LIGHT_WEIGHTED_PRESSURE_PLATE block" }
                            val stateId = when(power.toString()) {
"0" -> 6650
"1" -> 6651
"2" -> 6652
"3" -> 6653
"4" -> 6654
"5" -> 6655
"6" -> 6656
"7" -> 6657
"8" -> 6658
"9" -> 6659
"10" -> 6660
"11" -> 6661
"12" -> 6662
"13" -> 6663
"14" -> 6664
"15" -> 6665
                else -> 6650
            }
                return BlockInstance(this, stateId, mapOf("power" to power))
            }

            fun Block.HEAVY_WEIGHTED_PRESSURE_PLATE(power: Int = 0): BlockInstance {
                require(this == Block.HEAVY_WEIGHTED_PRESSURE_PLATE) { "Not a HEAVY_WEIGHTED_PRESSURE_PLATE block" }
                            val stateId = when(power.toString()) {
"0" -> 6666
"1" -> 6667
"2" -> 6668
"3" -> 6669
"4" -> 6670
"5" -> 6671
"6" -> 6672
"7" -> 6673
"8" -> 6674
"9" -> 6675
"10" -> 6676
"11" -> 6677
"12" -> 6678
"13" -> 6679
"14" -> 6680
"15" -> 6681
                else -> 6666
            }
                return BlockInstance(this, stateId, mapOf("power" to power))
            }

            fun Block.QUARTZ_PILLAR(axis: String = "y"): BlockInstance {
                require(this == Block.QUARTZ_PILLAR) { "Not a QUARTZ_PILLAR block" }
                            val stateId = when(axis.toString()) {
"x" -> 6744
"y" -> 6745
"z" -> 6746
                else -> 6745
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.HAY_BLOCK(axis: String = "y"): BlockInstance {
                require(this == Block.HAY_BLOCK) { "Not a HAY_BLOCK block" }
                            val stateId = when(axis.toString()) {
"x" -> 7867
"y" -> 7868
"z" -> 7869
                else -> 7868
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.SUNFLOWER(half: String = "lower"): BlockInstance {
                require(this == Block.SUNFLOWER) { "Not a SUNFLOWER block" }
                            val stateId = when(half.toString()) {
"upper" -> 7889
"lower" -> 7890
                else -> 7890
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.LILAC(half: String = "lower"): BlockInstance {
                require(this == Block.LILAC) { "Not a LILAC block" }
                            val stateId = when(half.toString()) {
"upper" -> 7891
"lower" -> 7892
                else -> 7892
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.ROSE_BUSH(half: String = "lower"): BlockInstance {
                require(this == Block.ROSE_BUSH) { "Not a ROSE_BUSH block" }
                            val stateId = when(half.toString()) {
"upper" -> 7893
"lower" -> 7894
                else -> 7894
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.PEONY(half: String = "lower"): BlockInstance {
                require(this == Block.PEONY) { "Not a PEONY block" }
                            val stateId = when(half.toString()) {
"upper" -> 7895
"lower" -> 7896
                else -> 7896
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.TALL_GRASS(half: String = "lower"): BlockInstance {
                require(this == Block.TALL_GRASS) { "Not a TALL_GRASS block" }
                            val stateId = when(half.toString()) {
"upper" -> 7897
"lower" -> 7898
                else -> 7898
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.LARGE_FERN(half: String = "lower"): BlockInstance {
                require(this == Block.LARGE_FERN) { "Not a LARGE_FERN block" }
                            val stateId = when(half.toString()) {
"upper" -> 7899
"lower" -> 7900
                else -> 7900
            }
                return BlockInstance(this, stateId, mapOf("half" to half))
            }

            fun Block.WHITE_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.WHITE_BANNER) { "Not a WHITE_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7901
"1" -> 7902
"2" -> 7903
"3" -> 7904
"4" -> 7905
"5" -> 7906
"6" -> 7907
"7" -> 7908
"8" -> 7909
"9" -> 7910
"10" -> 7911
"11" -> 7912
"12" -> 7913
"13" -> 7914
"14" -> 7915
"15" -> 7916
                else -> 7901
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.ORANGE_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.ORANGE_BANNER) { "Not a ORANGE_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7917
"1" -> 7918
"2" -> 7919
"3" -> 7920
"4" -> 7921
"5" -> 7922
"6" -> 7923
"7" -> 7924
"8" -> 7925
"9" -> 7926
"10" -> 7927
"11" -> 7928
"12" -> 7929
"13" -> 7930
"14" -> 7931
"15" -> 7932
                else -> 7917
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.MAGENTA_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.MAGENTA_BANNER) { "Not a MAGENTA_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7933
"1" -> 7934
"2" -> 7935
"3" -> 7936
"4" -> 7937
"5" -> 7938
"6" -> 7939
"7" -> 7940
"8" -> 7941
"9" -> 7942
"10" -> 7943
"11" -> 7944
"12" -> 7945
"13" -> 7946
"14" -> 7947
"15" -> 7948
                else -> 7933
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.LIGHT_BLUE_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.LIGHT_BLUE_BANNER) { "Not a LIGHT_BLUE_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7949
"1" -> 7950
"2" -> 7951
"3" -> 7952
"4" -> 7953
"5" -> 7954
"6" -> 7955
"7" -> 7956
"8" -> 7957
"9" -> 7958
"10" -> 7959
"11" -> 7960
"12" -> 7961
"13" -> 7962
"14" -> 7963
"15" -> 7964
                else -> 7949
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.YELLOW_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.YELLOW_BANNER) { "Not a YELLOW_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7965
"1" -> 7966
"2" -> 7967
"3" -> 7968
"4" -> 7969
"5" -> 7970
"6" -> 7971
"7" -> 7972
"8" -> 7973
"9" -> 7974
"10" -> 7975
"11" -> 7976
"12" -> 7977
"13" -> 7978
"14" -> 7979
"15" -> 7980
                else -> 7965
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.LIME_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.LIME_BANNER) { "Not a LIME_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7981
"1" -> 7982
"2" -> 7983
"3" -> 7984
"4" -> 7985
"5" -> 7986
"6" -> 7987
"7" -> 7988
"8" -> 7989
"9" -> 7990
"10" -> 7991
"11" -> 7992
"12" -> 7993
"13" -> 7994
"14" -> 7995
"15" -> 7996
                else -> 7981
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.PINK_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.PINK_BANNER) { "Not a PINK_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 7997
"1" -> 7998
"2" -> 7999
"3" -> 8000
"4" -> 8001
"5" -> 8002
"6" -> 8003
"7" -> 8004
"8" -> 8005
"9" -> 8006
"10" -> 8007
"11" -> 8008
"12" -> 8009
"13" -> 8010
"14" -> 8011
"15" -> 8012
                else -> 7997
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.GRAY_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.GRAY_BANNER) { "Not a GRAY_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8013
"1" -> 8014
"2" -> 8015
"3" -> 8016
"4" -> 8017
"5" -> 8018
"6" -> 8019
"7" -> 8020
"8" -> 8021
"9" -> 8022
"10" -> 8023
"11" -> 8024
"12" -> 8025
"13" -> 8026
"14" -> 8027
"15" -> 8028
                else -> 8013
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.LIGHT_GRAY_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.LIGHT_GRAY_BANNER) { "Not a LIGHT_GRAY_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8029
"1" -> 8030
"2" -> 8031
"3" -> 8032
"4" -> 8033
"5" -> 8034
"6" -> 8035
"7" -> 8036
"8" -> 8037
"9" -> 8038
"10" -> 8039
"11" -> 8040
"12" -> 8041
"13" -> 8042
"14" -> 8043
"15" -> 8044
                else -> 8029
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.CYAN_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.CYAN_BANNER) { "Not a CYAN_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8045
"1" -> 8046
"2" -> 8047
"3" -> 8048
"4" -> 8049
"5" -> 8050
"6" -> 8051
"7" -> 8052
"8" -> 8053
"9" -> 8054
"10" -> 8055
"11" -> 8056
"12" -> 8057
"13" -> 8058
"14" -> 8059
"15" -> 8060
                else -> 8045
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.PURPLE_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.PURPLE_BANNER) { "Not a PURPLE_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8061
"1" -> 8062
"2" -> 8063
"3" -> 8064
"4" -> 8065
"5" -> 8066
"6" -> 8067
"7" -> 8068
"8" -> 8069
"9" -> 8070
"10" -> 8071
"11" -> 8072
"12" -> 8073
"13" -> 8074
"14" -> 8075
"15" -> 8076
                else -> 8061
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.BLUE_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.BLUE_BANNER) { "Not a BLUE_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8077
"1" -> 8078
"2" -> 8079
"3" -> 8080
"4" -> 8081
"5" -> 8082
"6" -> 8083
"7" -> 8084
"8" -> 8085
"9" -> 8086
"10" -> 8087
"11" -> 8088
"12" -> 8089
"13" -> 8090
"14" -> 8091
"15" -> 8092
                else -> 8077
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.BROWN_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.BROWN_BANNER) { "Not a BROWN_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8093
"1" -> 8094
"2" -> 8095
"3" -> 8096
"4" -> 8097
"5" -> 8098
"6" -> 8099
"7" -> 8100
"8" -> 8101
"9" -> 8102
"10" -> 8103
"11" -> 8104
"12" -> 8105
"13" -> 8106
"14" -> 8107
"15" -> 8108
                else -> 8093
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.GREEN_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.GREEN_BANNER) { "Not a GREEN_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8109
"1" -> 8110
"2" -> 8111
"3" -> 8112
"4" -> 8113
"5" -> 8114
"6" -> 8115
"7" -> 8116
"8" -> 8117
"9" -> 8118
"10" -> 8119
"11" -> 8120
"12" -> 8121
"13" -> 8122
"14" -> 8123
"15" -> 8124
                else -> 8109
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.RED_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.RED_BANNER) { "Not a RED_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8125
"1" -> 8126
"2" -> 8127
"3" -> 8128
"4" -> 8129
"5" -> 8130
"6" -> 8131
"7" -> 8132
"8" -> 8133
"9" -> 8134
"10" -> 8135
"11" -> 8136
"12" -> 8137
"13" -> 8138
"14" -> 8139
"15" -> 8140
                else -> 8125
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.BLACK_BANNER(rotation: Int = 0): BlockInstance {
                require(this == Block.BLACK_BANNER) { "Not a BLACK_BANNER block" }
                            val stateId = when(rotation.toString()) {
"0" -> 8141
"1" -> 8142
"2" -> 8143
"3" -> 8144
"4" -> 8145
"5" -> 8146
"6" -> 8147
"7" -> 8148
"8" -> 8149
"9" -> 8150
"10" -> 8151
"11" -> 8152
"12" -> 8153
"13" -> 8154
"14" -> 8155
"15" -> 8156
                else -> 8141
            }
                return BlockInstance(this, stateId, mapOf("rotation" to rotation))
            }

            fun Block.WHITE_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.WHITE_WALL_BANNER) { "Not a WHITE_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8157
"south" -> 8158
"west" -> 8159
"east" -> 8160
                else -> 8157
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ORANGE_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.ORANGE_WALL_BANNER) { "Not a ORANGE_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8161
"south" -> 8162
"west" -> 8163
"east" -> 8164
                else -> 8161
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.MAGENTA_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.MAGENTA_WALL_BANNER) { "Not a MAGENTA_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8165
"south" -> 8166
"west" -> 8167
"east" -> 8168
                else -> 8165
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_BLUE_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.LIGHT_BLUE_WALL_BANNER) { "Not a LIGHT_BLUE_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8169
"south" -> 8170
"west" -> 8171
"east" -> 8172
                else -> 8169
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.YELLOW_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.YELLOW_WALL_BANNER) { "Not a YELLOW_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8173
"south" -> 8174
"west" -> 8175
"east" -> 8176
                else -> 8173
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIME_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.LIME_WALL_BANNER) { "Not a LIME_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8177
"south" -> 8178
"west" -> 8179
"east" -> 8180
                else -> 8177
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PINK_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.PINK_WALL_BANNER) { "Not a PINK_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8181
"south" -> 8182
"west" -> 8183
"east" -> 8184
                else -> 8181
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GRAY_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.GRAY_WALL_BANNER) { "Not a GRAY_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8185
"south" -> 8186
"west" -> 8187
"east" -> 8188
                else -> 8185
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_GRAY_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.LIGHT_GRAY_WALL_BANNER) { "Not a LIGHT_GRAY_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8189
"south" -> 8190
"west" -> 8191
"east" -> 8192
                else -> 8189
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CYAN_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.CYAN_WALL_BANNER) { "Not a CYAN_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8193
"south" -> 8194
"west" -> 8195
"east" -> 8196
                else -> 8193
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PURPLE_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.PURPLE_WALL_BANNER) { "Not a PURPLE_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8197
"south" -> 8198
"west" -> 8199
"east" -> 8200
                else -> 8197
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLUE_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.BLUE_WALL_BANNER) { "Not a BLUE_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8201
"south" -> 8202
"west" -> 8203
"east" -> 8204
                else -> 8201
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BROWN_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.BROWN_WALL_BANNER) { "Not a BROWN_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8205
"south" -> 8206
"west" -> 8207
"east" -> 8208
                else -> 8205
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GREEN_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.GREEN_WALL_BANNER) { "Not a GREEN_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8209
"south" -> 8210
"west" -> 8211
"east" -> 8212
                else -> 8209
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.RED_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.RED_WALL_BANNER) { "Not a RED_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8213
"south" -> 8214
"west" -> 8215
"east" -> 8216
                else -> 8213
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLACK_WALL_BANNER(facing: String = "north"): BlockInstance {
                require(this == Block.BLACK_WALL_BANNER) { "Not a BLACK_WALL_BANNER block" }
                            val stateId = when(facing.toString()) {
"north" -> 8217
"south" -> 8218
"west" -> 8219
"east" -> 8220
                else -> 8217
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.END_ROD(facing: String = "up"): BlockInstance {
                require(this == Block.END_ROD) { "Not a END_ROD block" }
                            val stateId = when(facing.toString()) {
"north" -> 9062
"east" -> 9063
"south" -> 9064
"west" -> 9065
"up" -> 9066
"down" -> 9067
                else -> 9066
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CHORUS_FLOWER(age: Int = 0): BlockInstance {
                require(this == Block.CHORUS_FLOWER) { "Not a CHORUS_FLOWER block" }
                            val stateId = when(age.toString()) {
"0" -> 9132
"1" -> 9133
"2" -> 9134
"3" -> 9135
"4" -> 9136
"5" -> 9137
                else -> 9132
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.PURPUR_PILLAR(axis: String = "y"): BlockInstance {
                require(this == Block.PURPUR_PILLAR) { "Not a PURPUR_PILLAR block" }
                            val stateId = when(axis.toString()) {
"x" -> 9139
"y" -> 9140
"z" -> 9141
                else -> 9140
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.BEETROOTS(age: Int = 0): BlockInstance {
                require(this == Block.BEETROOTS) { "Not a BEETROOTS block" }
                            val stateId = when(age.toString()) {
"0" -> 9223
"1" -> 9224
"2" -> 9225
"3" -> 9226
                else -> 9223
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.FROSTED_ICE(age: Int = 0): BlockInstance {
                require(this == Block.FROSTED_ICE) { "Not a FROSTED_ICE block" }
                            val stateId = when(age.toString()) {
"0" -> 9253
"1" -> 9254
"2" -> 9255
"3" -> 9256
                else -> 9253
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.BONE_BLOCK(axis: String = "y"): BlockInstance {
                require(this == Block.BONE_BLOCK) { "Not a BONE_BLOCK block" }
                            val stateId = when(axis.toString()) {
"x" -> 9260
"y" -> 9261
"z" -> 9262
                else -> 9261
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.SHULKER_BOX) { "Not a SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9276
"east" -> 9277
"south" -> 9278
"west" -> 9279
"up" -> 9280
"down" -> 9281
                else -> 9280
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.WHITE_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.WHITE_SHULKER_BOX) { "Not a WHITE_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9282
"east" -> 9283
"south" -> 9284
"west" -> 9285
"up" -> 9286
"down" -> 9287
                else -> 9286
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ORANGE_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.ORANGE_SHULKER_BOX) { "Not a ORANGE_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9288
"east" -> 9289
"south" -> 9290
"west" -> 9291
"up" -> 9292
"down" -> 9293
                else -> 9292
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.MAGENTA_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.MAGENTA_SHULKER_BOX) { "Not a MAGENTA_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9294
"east" -> 9295
"south" -> 9296
"west" -> 9297
"up" -> 9298
"down" -> 9299
                else -> 9298
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_BLUE_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.LIGHT_BLUE_SHULKER_BOX) { "Not a LIGHT_BLUE_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9300
"east" -> 9301
"south" -> 9302
"west" -> 9303
"up" -> 9304
"down" -> 9305
                else -> 9304
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.YELLOW_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.YELLOW_SHULKER_BOX) { "Not a YELLOW_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9306
"east" -> 9307
"south" -> 9308
"west" -> 9309
"up" -> 9310
"down" -> 9311
                else -> 9310
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIME_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.LIME_SHULKER_BOX) { "Not a LIME_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9312
"east" -> 9313
"south" -> 9314
"west" -> 9315
"up" -> 9316
"down" -> 9317
                else -> 9316
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PINK_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.PINK_SHULKER_BOX) { "Not a PINK_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9318
"east" -> 9319
"south" -> 9320
"west" -> 9321
"up" -> 9322
"down" -> 9323
                else -> 9322
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GRAY_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.GRAY_SHULKER_BOX) { "Not a GRAY_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9324
"east" -> 9325
"south" -> 9326
"west" -> 9327
"up" -> 9328
"down" -> 9329
                else -> 9328
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_GRAY_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.LIGHT_GRAY_SHULKER_BOX) { "Not a LIGHT_GRAY_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9330
"east" -> 9331
"south" -> 9332
"west" -> 9333
"up" -> 9334
"down" -> 9335
                else -> 9334
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CYAN_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.CYAN_SHULKER_BOX) { "Not a CYAN_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9336
"east" -> 9337
"south" -> 9338
"west" -> 9339
"up" -> 9340
"down" -> 9341
                else -> 9340
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PURPLE_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.PURPLE_SHULKER_BOX) { "Not a PURPLE_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9342
"east" -> 9343
"south" -> 9344
"west" -> 9345
"up" -> 9346
"down" -> 9347
                else -> 9346
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLUE_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.BLUE_SHULKER_BOX) { "Not a BLUE_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9348
"east" -> 9349
"south" -> 9350
"west" -> 9351
"up" -> 9352
"down" -> 9353
                else -> 9352
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BROWN_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.BROWN_SHULKER_BOX) { "Not a BROWN_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9354
"east" -> 9355
"south" -> 9356
"west" -> 9357
"up" -> 9358
"down" -> 9359
                else -> 9358
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GREEN_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.GREEN_SHULKER_BOX) { "Not a GREEN_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9360
"east" -> 9361
"south" -> 9362
"west" -> 9363
"up" -> 9364
"down" -> 9365
                else -> 9364
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.RED_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.RED_SHULKER_BOX) { "Not a RED_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9366
"east" -> 9367
"south" -> 9368
"west" -> 9369
"up" -> 9370
"down" -> 9371
                else -> 9370
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLACK_SHULKER_BOX(facing: String = "up"): BlockInstance {
                require(this == Block.BLACK_SHULKER_BOX) { "Not a BLACK_SHULKER_BOX block" }
                            val stateId = when(facing.toString()) {
"north" -> 9372
"east" -> 9373
"south" -> 9374
"west" -> 9375
"up" -> 9376
"down" -> 9377
                else -> 9376
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.WHITE_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.WHITE_GLAZED_TERRACOTTA) { "Not a WHITE_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9378
"south" -> 9379
"west" -> 9380
"east" -> 9381
                else -> 9378
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.ORANGE_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.ORANGE_GLAZED_TERRACOTTA) { "Not a ORANGE_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9382
"south" -> 9383
"west" -> 9384
"east" -> 9385
                else -> 9382
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.MAGENTA_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.MAGENTA_GLAZED_TERRACOTTA) { "Not a MAGENTA_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9386
"south" -> 9387
"west" -> 9388
"east" -> 9389
                else -> 9386
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_BLUE_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.LIGHT_BLUE_GLAZED_TERRACOTTA) { "Not a LIGHT_BLUE_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9390
"south" -> 9391
"west" -> 9392
"east" -> 9393
                else -> 9390
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.YELLOW_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.YELLOW_GLAZED_TERRACOTTA) { "Not a YELLOW_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9394
"south" -> 9395
"west" -> 9396
"east" -> 9397
                else -> 9394
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIME_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.LIME_GLAZED_TERRACOTTA) { "Not a LIME_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9398
"south" -> 9399
"west" -> 9400
"east" -> 9401
                else -> 9398
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PINK_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.PINK_GLAZED_TERRACOTTA) { "Not a PINK_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9402
"south" -> 9403
"west" -> 9404
"east" -> 9405
                else -> 9402
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GRAY_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.GRAY_GLAZED_TERRACOTTA) { "Not a GRAY_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9406
"south" -> 9407
"west" -> 9408
"east" -> 9409
                else -> 9406
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.LIGHT_GRAY_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.LIGHT_GRAY_GLAZED_TERRACOTTA) { "Not a LIGHT_GRAY_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9410
"south" -> 9411
"west" -> 9412
"east" -> 9413
                else -> 9410
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.CYAN_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.CYAN_GLAZED_TERRACOTTA) { "Not a CYAN_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9414
"south" -> 9415
"west" -> 9416
"east" -> 9417
                else -> 9414
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.PURPLE_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.PURPLE_GLAZED_TERRACOTTA) { "Not a PURPLE_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9418
"south" -> 9419
"west" -> 9420
"east" -> 9421
                else -> 9418
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLUE_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.BLUE_GLAZED_TERRACOTTA) { "Not a BLUE_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9422
"south" -> 9423
"west" -> 9424
"east" -> 9425
                else -> 9422
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BROWN_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.BROWN_GLAZED_TERRACOTTA) { "Not a BROWN_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9426
"south" -> 9427
"west" -> 9428
"east" -> 9429
                else -> 9426
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.GREEN_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.GREEN_GLAZED_TERRACOTTA) { "Not a GREEN_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9430
"south" -> 9431
"west" -> 9432
"east" -> 9433
                else -> 9430
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.RED_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.RED_GLAZED_TERRACOTTA) { "Not a RED_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9434
"south" -> 9435
"west" -> 9436
"east" -> 9437
                else -> 9434
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.BLACK_GLAZED_TERRACOTTA(facing: String = "north"): BlockInstance {
                require(this == Block.BLACK_GLAZED_TERRACOTTA) { "Not a BLACK_GLAZED_TERRACOTTA block" }
                            val stateId = when(facing.toString()) {
"north" -> 9438
"south" -> 9439
"west" -> 9440
"east" -> 9441
                else -> 9438
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.KELP(age: Int = 0): BlockInstance {
                require(this == Block.KELP) { "Not a KELP block" }
                            val stateId = when(age.toString()) {
"0" -> 9474
"1" -> 9475
"2" -> 9476
"3" -> 9477
"4" -> 9478
"5" -> 9479
"6" -> 9480
"7" -> 9481
"8" -> 9482
"9" -> 9483
"10" -> 9484
"11" -> 9485
"12" -> 9486
"13" -> 9487
"14" -> 9488
"15" -> 9489
"16" -> 9490
"17" -> 9491
"18" -> 9492
"19" -> 9493
"20" -> 9494
"21" -> 9495
"22" -> 9496
"23" -> 9497
"24" -> 9498
"25" -> 9499
                else -> 9474
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

fun Block.DEAD_TUBE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_TUBE_CORAL) { "Not a DEAD_TUBE_CORAL block" }
    val stateId = if (waterlogged) 9524 else 9525
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_BRAIN_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_BRAIN_CORAL) { "Not a DEAD_BRAIN_CORAL block" }
    val stateId = if (waterlogged) 9526 else 9527
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_BUBBLE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_BUBBLE_CORAL) { "Not a DEAD_BUBBLE_CORAL block" }
    val stateId = if (waterlogged) 9528 else 9529
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_FIRE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_FIRE_CORAL) { "Not a DEAD_FIRE_CORAL block" }
    val stateId = if (waterlogged) 9530 else 9531
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_HORN_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_HORN_CORAL) { "Not a DEAD_HORN_CORAL block" }
    val stateId = if (waterlogged) 9532 else 9533
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.TUBE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.TUBE_CORAL) { "Not a TUBE_CORAL block" }
    val stateId = if (waterlogged) 9534 else 9535
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.BRAIN_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.BRAIN_CORAL) { "Not a BRAIN_CORAL block" }
    val stateId = if (waterlogged) 9536 else 9537
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.BUBBLE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.BUBBLE_CORAL) { "Not a BUBBLE_CORAL block" }
    val stateId = if (waterlogged) 9538 else 9539
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.FIRE_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.FIRE_CORAL) { "Not a FIRE_CORAL block" }
    val stateId = if (waterlogged) 9540 else 9541
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.HORN_CORAL(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.HORN_CORAL) { "Not a HORN_CORAL block" }
    val stateId = if (waterlogged) 9542 else 9543
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_TUBE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_TUBE_CORAL_FAN) { "Not a DEAD_TUBE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9544 else 9545
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_BRAIN_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_BRAIN_CORAL_FAN) { "Not a DEAD_BRAIN_CORAL_FAN block" }
    val stateId = if (waterlogged) 9546 else 9547
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_BUBBLE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_BUBBLE_CORAL_FAN) { "Not a DEAD_BUBBLE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9548 else 9549
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_FIRE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_FIRE_CORAL_FAN) { "Not a DEAD_FIRE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9550 else 9551
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.DEAD_HORN_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.DEAD_HORN_CORAL_FAN) { "Not a DEAD_HORN_CORAL_FAN block" }
    val stateId = if (waterlogged) 9552 else 9553
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.TUBE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.TUBE_CORAL_FAN) { "Not a TUBE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9554 else 9555
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.BRAIN_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.BRAIN_CORAL_FAN) { "Not a BRAIN_CORAL_FAN block" }
    val stateId = if (waterlogged) 9556 else 9557
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.BUBBLE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.BUBBLE_CORAL_FAN) { "Not a BUBBLE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9558 else 9559
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.FIRE_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.FIRE_CORAL_FAN) { "Not a FIRE_CORAL_FAN block" }
    val stateId = if (waterlogged) 9560 else 9561
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.HORN_CORAL_FAN(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.HORN_CORAL_FAN) { "Not a HORN_CORAL_FAN block" }
    val stateId = if (waterlogged) 9562 else 9563
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.CONDUIT(waterlogged: Boolean = true): BlockInstance {
    require(this == Block.CONDUIT) { "Not a CONDUIT block" }
    val stateId = if (waterlogged) 9653 else 9654
    return BlockInstance(this, stateId, mapOf("waterlogged" to waterlogged))
}

fun Block.BUBBLE_COLUMN(drag: Boolean = true): BlockInstance {
    require(this == Block.BUBBLE_COLUMN) { "Not a BUBBLE_COLUMN block" }
    val stateId = if (drag) 9671 else 9672
    return BlockInstance(this, stateId, mapOf("drag" to drag))
}

            fun Block.LOOM(facing: String = "north"): BlockInstance {
                require(this == Block.LOOM) { "Not a LOOM block" }
                            val stateId = when(facing.toString()) {
"north" -> 14791
"south" -> 14792
"west" -> 14793
"east" -> 14794
                else -> 14791
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.STONECUTTER(facing: String = "north"): BlockInstance {
                require(this == Block.STONECUTTER) { "Not a STONECUTTER block" }
                            val stateId = when(facing.toString()) {
"north" -> 14854
"south" -> 14855
"west" -> 14856
"east" -> 14857
                else -> 14854
            }
                return BlockInstance(this, stateId, mapOf("facing" to facing))
            }

            fun Block.SWEET_BERRY_BUSH(age: Int = 0): BlockInstance {
                require(this == Block.SWEET_BERRY_BUSH) { "Not a SWEET_BERRY_BUSH block" }
                            val stateId = when(age.toString()) {
"0" -> 14962
"1" -> 14963
"2" -> 14964
"3" -> 14965
                else -> 14962
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.WARPED_STEM(axis: String = "y"): BlockInstance {
                require(this == Block.WARPED_STEM) { "Not a WARPED_STEM block" }
                            val stateId = when(axis.toString()) {
"x" -> 14966
"y" -> 14967
"z" -> 14968
                else -> 14967
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_WARPED_STEM(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_WARPED_STEM) { "Not a STRIPPED_WARPED_STEM block" }
                            val stateId = when(axis.toString()) {
"x" -> 14969
"y" -> 14970
"z" -> 14971
                else -> 14970
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.WARPED_HYPHAE(axis: String = "y"): BlockInstance {
                require(this == Block.WARPED_HYPHAE) { "Not a WARPED_HYPHAE block" }
                            val stateId = when(axis.toString()) {
"x" -> 14972
"y" -> 14973
"z" -> 14974
                else -> 14973
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_WARPED_HYPHAE(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_WARPED_HYPHAE) { "Not a STRIPPED_WARPED_HYPHAE block" }
                            val stateId = when(axis.toString()) {
"x" -> 14975
"y" -> 14976
"z" -> 14977
                else -> 14976
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.CRIMSON_STEM(axis: String = "y"): BlockInstance {
                require(this == Block.CRIMSON_STEM) { "Not a CRIMSON_STEM block" }
                            val stateId = when(axis.toString()) {
"x" -> 14983
"y" -> 14984
"z" -> 14985
                else -> 14984
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_CRIMSON_STEM(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_CRIMSON_STEM) { "Not a STRIPPED_CRIMSON_STEM block" }
                            val stateId = when(axis.toString()) {
"x" -> 14986
"y" -> 14987
"z" -> 14988
                else -> 14987
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.CRIMSON_HYPHAE(axis: String = "y"): BlockInstance {
                require(this == Block.CRIMSON_HYPHAE) { "Not a CRIMSON_HYPHAE block" }
                            val stateId = when(axis.toString()) {
"x" -> 14989
"y" -> 14990
"z" -> 14991
                else -> 14990
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.STRIPPED_CRIMSON_HYPHAE(axis: String = "y"): BlockInstance {
                require(this == Block.STRIPPED_CRIMSON_HYPHAE) { "Not a STRIPPED_CRIMSON_HYPHAE block" }
                            val stateId = when(axis.toString()) {
"x" -> 14992
"y" -> 14993
"z" -> 14994
                else -> 14993
            }
                return BlockInstance(this, stateId, mapOf("axis" to axis))
            }

            fun Block.WEEPING_VINES(age: Int = 0): BlockInstance {
                require(this == Block.WEEPING_VINES) { "Not a WEEPING_VINES block" }
                            val stateId = when(age.toString()) {
"0" -> 14998
"1" -> 14999
"2" -> 15000
"3" -> 15001
"4" -> 15002
"5" -> 15003
"6" -> 15004
"7" -> 15005
"8" -> 15006
"9" -> 15007
"10" -> 15008
"11" -> 15009
"12" -> 15010
"13" -> 15011
"14" -> 15012
"15" -> 15013
"16" -> 15014
"17" -> 15015
"18" -> 15016
"19" -> 15017
"20" -> 15018
"21" -> 15019
"22" -> 15020
"23" -> 15021
"24" -> 15022
"25" -> 15023
                else -> 14998
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

            fun Block.TWISTING_VINES(age: Int = 0): BlockInstance {
                require(this == Block.TWISTING_VINES) { "Not a TWISTING_VINES block" }
                            val stateId = when(age.toString()) {
"0" -> 15025
"1" -> 15026
"2" -> 15027
"3" -> 15028
"4" -> 15029
"5" -> 15030
"6" -> 15031
"7" -> 15032
"8" -> 15033
"9" -> 15034
"10" -> 15035
"11" -> 15036
"12" -> 15037
"13" -> 15038
"14" -> 15039
"15" -> 15040
"16" -> 15041
"17" -> 15042
"18" -> 15043
"19" -> 15044
"20" -> 15045
"21" -> 15046
"22" -> 15047
"23" -> 15048
"24" -> 15049
"25" -> 15050
                else -> 15025
            }
                return BlockInstance(this, stateId, mapOf("age" to age))
            }

fun Block.CRIMSON_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.CRIMSON_PRESSURE_PLATE) { "Not a CRIMSON_PRESSURE_PLATE block" }
    val stateId = if (powered) 15067 else 15068
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

fun Block.WARPED_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.WARPED_PRESSURE_PLATE) { "Not a WARPED_PRESSURE_PLATE block" }
    val stateId = if (powered) 15069 else 15070
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}

            fun Block.STRUCTURE_BLOCK(mode: String = "save"): BlockInstance {
                require(this == Block.STRUCTURE_BLOCK) { "Not a STRUCTURE_BLOCK block" }
                            val stateId = when(mode.toString()) {
"save" -> 15743
"load" -> 15744
"corner" -> 15745
"data" -> 15746
                else -> 15743
            }
                return BlockInstance(this, stateId, mapOf("mode" to mode))
            }

            fun Block.JIGSAW(orientation: String = "north_up"): BlockInstance {
                require(this == Block.JIGSAW) { "Not a JIGSAW block" }
                            val stateId = when(orientation.toString()) {
"down_east" -> 15747
"down_north" -> 15748
"down_south" -> 15749
"down_west" -> 15750
"up_east" -> 15751
"up_north" -> 15752
"up_south" -> 15753
"up_west" -> 15754
"west_up" -> 15755
"east_up" -> 15756
"north_up" -> 15757
"south_up" -> 15758
                else -> 15757
            }
                return BlockInstance(this, stateId, mapOf("orientation" to orientation))
            }

            fun Block.COMPOSTER(level: Int = 0): BlockInstance {
                require(this == Block.COMPOSTER) { "Not a COMPOSTER block" }
                            val stateId = when(level.toString()) {
"0" -> 15759
"1" -> 15760
"2" -> 15761
"3" -> 15762
"4" -> 15763
"5" -> 15764
"6" -> 15765
"7" -> 15766
"8" -> 15767
                else -> 15759
            }
                return BlockInstance(this, stateId, mapOf("level" to level))
            }

            fun Block.TARGET(power: Int = 0): BlockInstance {
                require(this == Block.TARGET) { "Not a TARGET block" }
                            val stateId = when(power.toString()) {
"0" -> 15768
"1" -> 15769
"2" -> 15770
"3" -> 15771
"4" -> 15772
"5" -> 15773
"6" -> 15774
"7" -> 15775
"8" -> 15776
"9" -> 15777
"10" -> 15778
"11" -> 15779
"12" -> 15780
"13" -> 15781
"14" -> 15782
"15" -> 15783
                else -> 15768
            }
                return BlockInstance(this, stateId, mapOf("power" to power))
            }

            fun Block.RESPAWN_ANCHOR(charges: Int = 0): BlockInstance {
                require(this == Block.RESPAWN_ANCHOR) { "Not a RESPAWN_ANCHOR block" }
                            val stateId = when(charges.toString()) {
"0" -> 15837
"1" -> 15838
"2" -> 15839
"3" -> 15840
"4" -> 15841
                else -> 15837
            }
                return BlockInstance(this, stateId, mapOf("charges" to charges))
            }

fun Block.POLISHED_BLACKSTONE_PRESSURE_PLATE(powered: Boolean = false): BlockInstance {
    require(this == Block.POLISHED_BLACKSTONE_PRESSURE_PLATE) { "Not a POLISHED_BLACKSTONE_PRESSURE_PLATE block" }
    val stateId = if (powered) 16759 else 16760
    return BlockInstance(this, stateId, mapOf("powered" to powered))
}
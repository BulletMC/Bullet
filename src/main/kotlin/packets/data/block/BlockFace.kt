package com.aznos.packets.data.block

enum class BlockFace {

    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    OTHER(255, -1, -1, -1);

    val faceValue: Short
    val modX: Int
    val modY: Int
    val modZ: Int

    constructor(faceValue: Short, modX: Int, modY: Int, modZ: Int) {
        this.faceValue = faceValue
        this.modX = modX
        this.modY = modY
        this.modZ = modZ
    }

    constructor(modX: Int, modY: Int, modZ: Int) {
        this.faceValue = ordinal.toShort()
        this.modX = modX
        this.modY = modY
        this.modZ = modZ
    }

    val opposite: BlockFace
        get() {
            return when (this) {
                DOWN -> UP
                UP -> DOWN
                NORTH -> SOUTH
                SOUTH -> NORTH
                WEST -> EAST
                EAST -> WEST
                OTHER -> OTHER
            }
        }

    init {

    }
}
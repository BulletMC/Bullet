package com.aznos.world.data.anvil

data class AnvilSection(val y: Int, val palette: MutableList<PaletteEntry>, var bitsPerBlock: Int, var blockStates: LongArray) {
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as AnvilSection

        if(y != other.y) return false
        if(bitsPerBlock != other.bitsPerBlock) return false
        if(palette != other.palette) return false
        if(!blockStates.contentEquals(other.blockStates)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + bitsPerBlock
        result = 31 * result + palette.hashCode()
        result = 31 * result + blockStates.contentHashCode()
        return result
    }
}

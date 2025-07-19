package com.aznos.world.anvil

object PaletteIndex {
    fun getPaletteIndex(arr: LongArray, bits: Int, idx: Int): Int {
        val bitIndex = idx * bits
        val longIndex = bitIndex ushr 6
        val startBit = bitIndex and 63
        var value = arr[longIndex] ushr startBit
        val spill = startBit + bits - 64
        if(spill > 0) value = value or (arr[longIndex + 1] shl (bits - spill))
        val mask = (1L shl bits) - 1
        return (value and mask).toInt()
    }

    fun setPaletteIndex(arr: LongArray, bits: Int, idx: Int, paletteIndex: Int) {
        val bitIndex = idx * bits
        val longIndex = bitIndex ushr 6
        val startBit = bitIndex and 63
        val mask = ((1L shl bits) - 1) shl startBit
        arr[longIndex] = (arr[longIndex] and mask.inv()) or ((paletteIndex.toLong() shl startBit))
        val spill = startBit + bits - 64
        if(spill > 0) {
            val lowBits = paletteIndex ushr (bits - spill)
            val mask2 = (1L shl spill) - 1
            arr[longIndex + 1] = (arr[longIndex + 1] and mask2.inv()) or lowBits.toLong()
        }
    }
}
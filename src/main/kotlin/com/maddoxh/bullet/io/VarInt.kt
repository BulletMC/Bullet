package com.maddoxh.bullet.io

object VarInt {
    fun varIntSize(value: Int): Int = when {
        value and (0.inv() shl 7)  == 0 -> 1
        value and (0.inv() shl 14) == 0 -> 2
        value and (0.inv() shl 21) == 0 -> 3
        value and (0.inv() shl 28) == 0 -> 4
        else                            -> 5
    }
}
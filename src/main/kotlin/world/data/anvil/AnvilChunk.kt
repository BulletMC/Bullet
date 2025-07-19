package com.aznos.world.data.anvil

data class AnvilChunk(val cx: Int, val cz: Int, val sections: MutableMap<Int, AnvilSection>, var dirty: Boolean = false)

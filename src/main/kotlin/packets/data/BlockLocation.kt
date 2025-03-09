package com.aznos.packets.data

import com.aznos.packets.newPacket.ResourceLocation

class BlockLocation(
    var world: ResourceLocation,
    var pos: Pos3i
) {
}
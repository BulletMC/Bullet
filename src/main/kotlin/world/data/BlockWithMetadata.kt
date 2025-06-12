package com.aznos.world.data

import kotlinx.serialization.Serializable

@Serializable
data class BlockWithMetadata(
    val blockID: Int,
    val stateID: Int,
    val textLines: List<String>? = null
)

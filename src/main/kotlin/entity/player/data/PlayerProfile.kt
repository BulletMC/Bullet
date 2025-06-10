package com.aznos.entity.player.data

import kotlinx.serialization.Serializable

@Serializable
data class PlayerProfile(
    val id: String,
    val name: String,
    val properties: List<Property>
) {
    @Serializable
    data class Property(
        val name: String,
        val value: String,
        val signature: String? = null
    )
}

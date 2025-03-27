package com.aznos.entity.player.data

import com.aznos.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.time.Duration

/**
 * Represents data about a player being banned
 */
@Serializable
data class BanData(
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val reason: String,
    val duration: Duration,
    @Serializable(with = UUIDSerializer::class) val bannedBy: UUID
)
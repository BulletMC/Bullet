package com.aznos.packets.data

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

/**
 * Data class representing the server status response
 *
 * @property version The server version
 * @property players The player count details
 * @property description The MOTD of the server
 * @property favicon The base64 encoded favicon image
 * @property enforcesSecureChat Whether secure chat is enforced
 */
@Serializable
data class ServerStatusResponse(
    val version: Version,
    val players: Players,
    val description: Component,
    val favicon: String? = null,
    val enforcesSecureChat: Boolean
) {
    /**
     * Data class representing the server version details
     *
     * @property name The version name
     * @property protocol The protocol number
     */
    @Serializable
    data class Version(
        val name: String,
        val protocol: Int
    )

    /**
     * Data class representing player count information
     *
     * @property max The maximum number of players that can connect
     * @property online The current number of players online
     */
    @Serializable
    data class Players(
        val max: Int,
        val online: Int
    )
}
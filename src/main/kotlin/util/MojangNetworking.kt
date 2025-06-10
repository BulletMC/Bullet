package com.aznos.util

import com.aznos.Bullet
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PlayerProfile
import com.aznos.entity.player.data.PlayerProperty
import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Utility object for networking with Mojang's session server.
 * Provides functionality to query player profiles based on username and server hash
 */
object MojangNetworking {
    const val SESSION_SERVER_URL = "https://sessionserver.mojang.com/session/minecraft"

    /**
     * Queries the Mojang session server for a player's profile based on their username and server hash.
     * Optionally includes the player's IP address.
     *
     * @param username The player's username.
     * @param hash The server hash.
     * @param ip Optional IP address of the player.
     * @return A PlayerProfile object if found, null otherwise.
     */
    suspend fun querySessionServer(player: Player, hash: String, ip: String? = null): PlayerProfile? {
        val url = buildString {
            append("$SESSION_SERVER_URL/hasJoined")
            append("?username=${player.username}")
            append("&serverId=$hash")
            if(ip != null) {
                append("&ip=$ip")
            }
        }

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url)).GET().build()

        val json = Json { ignoreUnknownKeys = true }
        val body = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .await()
            .body()

        if(body.isEmpty()) return null
        val profile = json.decodeFromString<PlayerProfile>(body)

        val props = profile.properties.map {
            PlayerProperty(
                name = it.name,
                value = it.value,
                isSigned = it.signature != null,
                signature = it.signature
            )
        }

        player.properties.clear()
        player.properties.addAll(props)

        return profile
    }
}
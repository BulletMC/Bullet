package com.aznos.events

import com.aznos.GameState
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.Position

//Unmodifiable events

/**
 * Called right when the server receives that the client is ready to play and
 * before any checks are made to see if the client is allowed to join
 */
class PlayerPreJoinEvent : Event()

/**
 * Called when a player joins the server
 *
 * @param username The username of the player that joined
 */
data class PlayerJoinEvent(val username: String) : Event()

/**
 * Called when a player leaves the server
 *
 * @param username The username of the player that left
 */
data class PlayerQuitEvent(val username: String) : Event()

/**
 * Called whenever there is a handshake between the client and server
 *
 * @param state The current game state, handshakes are either in the status or login state
 * @param protocol The protocol version of the client
 */
data class HandshakeEvent(val state: GameState, val protocol: Int) : Event()

/**
 * Called once a heartbeat has been completed, cancelling this event will result in the client being kicked
 *
 * @param username The username of the player
 */
data class PlayerHeartbeatEvent(val username: String) : Event()

/**
 * Called when a player sends a chat message
 *
 * @param username The username of the player that sent the message
 * @param message The message that was sent
 */
data class PlayerChatEvent(val username: String, val message: String) : Event()

/**
 * Called when a player sprints
 *
 * @param username The username of the player that is sprinting
 * @param isSprinting True if they're now sprinting, false if they stopped
 */
data class PlayerSprintEvent(val username: String, val isSprinting: Boolean) : Event()

/**
 * Called when a player sneaks
 *
 * @param username The username of the player that is sneaking
 * @param isSneaking True if they're now sneaking, false if they stopped
 */
data class PlayerSneakEvent(val username: String, val isSneaking: Boolean) : Event()

//Modifiable events

/**
 * Called when the server receives a status request packet to display server information
 *
 * @param maxPlayers The maximum amount of player allowed to join the server
 * @param onlinePlayers The amount of players currently online
 * @param motd The description of the server
 */
data class StatusRequestEvent(var maxPlayers: Int, var onlinePlayers: Int, var motd: String) : Event()

/**
 * Called when the server receives a dig packet from the client
 *
 * @param player The player that is breaking the block
 * @param status (0-9) The progress of the block break, if it is anything but 0-9 the block can be considered broken
 * @param location The position of the block
 * @param face The face of the block being hit
 */
data class BlockBreakEvent(
    val player: Player,
    var status: Int,
    var location: Position,
    val face: Int
) : Event()

/**
 * Called when the server receives a block place packet from the client
 *
 * @param player The player that is placing the block
 * @param hand Which hand the block was placed with
 * @param location The position of the block being placed
 * @param face The face of the block being placed
 * @param cursorPosX The X position of the crosshair on the block, 0-1
 * @param cursorPosY The Y position of the crosshair on the block, 0-1
 * @param cursorPosZ The Z position of the crosshair on the block, 0-1
 * @param insideBlock Whether the players head is inside a block
 */
data class BlockPlaceEvent(
    val player: Player,
    var hand: Int,
    var location: Position,
    val face: Int,
    val cursorPosX: Float,
    val cursorPosY: Float,
    val cursorPosZ: Float,
    val insideBlock: Boolean
) : Event()
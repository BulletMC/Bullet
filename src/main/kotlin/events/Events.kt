package com.aznos.events

import com.aznos.GameState
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.Location
import com.aznos.entity.player.data.Position

/**
 * Called right when the server receives that the client is ready to play and
 * before any checks are made to see if the client is allowed to join
 */
class PlayerPreJoinEvent : Event()

/**
 * Called when a player joins the server
 *
 * @param player The player that just joined
 */
data class PlayerJoinEvent(val player: Player) : Event()

/**
 * Called when a player leaves the server
 *
 * @param username The player that left
 */
data class PlayerQuitEvent(val player: Player) : Event()

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
 * @param player The player that sent the heartbeat packet
 */
data class PlayerHeartbeatEvent(val player: Player) : Event()

/**
 * Called when a player sends a chat message
 *
 * @param username The username of the player that sent the message
 * @param message The message that was sent
 */
data class PlayerChatEvent(var username: Player, var message: String) : Event()

/**
 * Called when a player sprints
 *
 * @param username The username of the player that is sprinting
 * @param isSprinting True if they're now sprinting, false if they stopped
 */
data class PlayerSprintEvent(val username: Player, var isSprinting: Boolean) : Event()

/**
 * Called when a player sneaks
 *
 * @param player The player that is sneaking
 * @param isSneaking True if they're now sneaking, false if they stopped
 */
data class PlayerSneakEvent(val player: Player, var isSneaking: Boolean) : Event()

/**
 * Sent right after the player joins the games and informs the server what brand the client is running
 * For the default vanilla client, this is just "vanilla"
 *
 * Cancelling this event will kick the player
 *
 * @param player The player
 * @param brand The brand the client is running
 */
data class PlayerBrandEvent(val player: Player, var brand: String) : Event()

/**
 * Sent anytime the player moves or rotates their head
 *
 * @param player The player who moved
 * @param location The new location of the player
 * @param prevLocation The previous location of the player
 */
data class PlayerMoveEvent(val player: Player, var location: Location, var prevLocation: Location) : Event()

/**
 * Called when a player interacts with an entity
 *
 * @param player The player that interacted with the entity
 * @param entityId The entity id of the entity that was interacted with
 * @param action 0: Interact, 1: Attack, 2: Interact At
 */
data class PlayerInteractEntityEvent(val player: Player, val entityId: Int, val action: Int) : Event()

/**
 * Called when a player changes their held item slot
 *
 * @param player The player that changed their held item slot
 * @param slot The new slot the player is holding
 */
data class PlayerHeldItemChangeEvent(val player: Player, var slot: Int) : Event()

/**
 * Called when a player changes their settings
 *
 * @param player The player that changed their settings
 * @param locale The language locale of the client (e.g. "en_US")
 * @param viewDistance The client's view distance setting
 * @param chatMode 0: Enabled, 1: Commands only, 2: Hidden
 * @param chatColors Whether the client has chat colors enabled
 * @param displayedSkinParts A bitmask representing which parts of the player's skin are displayed
 * @param mainHand 0: Left, 1: Right
 */
data class PlayerSettingsChangeEvent(
    val player: Player,
    var locale: String,
    var viewDistance: Int,
    var chatMode: Int,
    var chatColors: Boolean,
    var displayedSkinParts: Int,
    var mainHand: Int
) : Event()

/**
 * Called when a player swings their arm
 *
 * @param player The player that swung their arm
 */
data class PlayerArmSwingEvent(val player: Player) : Event()

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
    var face: Int,
    var cursorPosX: Float,
    var cursorPosY: Float,
    var cursorPosZ: Float,
    var insideBlock: Boolean
) : Event()
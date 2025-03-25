package com.aznos.entity.player

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.entity.Entity
import com.aznos.entity.player.data.ChatPosition
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.Location
import com.aznos.packets.Packet
import com.aznos.entity.player.data.PlayerProperty
import com.aznos.packets.play.out.*
import com.aznos.world.World
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import java.util.UUID

/**
 * Represents a player in the game
 *
 * @property clientSession The client session associated with the player
 */
class Player(
    val clientSession: ClientSession
) : Entity() {
    lateinit var username: String
    override lateinit var uuid: UUID
    lateinit var location: Location
    lateinit var locale: String
    lateinit var brand: String
    var world: World? = Bullet.world

    //Inventory and Equipment
    var inventory = Inventory()
    var selectedSlot: Int = 0

    //Player attributes
    var properties = mutableListOf<PlayerProperty>()
    var gameMode: GameMode = GameMode.CREATIVE
        private set
    var viewDistance: Int = 0
    var isSneaking: Boolean = false
    var ping: Int = 0
    var lastOnGroundY: Double = 0.0
    var fallDistance: Double = 0.0
    private var tabListHeader = Component.text()
    private var tabListFooter = Component.text()

    //Movement
    var onGround: Boolean = true
    var chunkX: Int = 0
    var chunkZ: Int = 0
    var lastSprintLocation: Location? = null
    val loadedChunks = mutableSetOf<Pair<Int, Int>>()

    //Combat and status
    var status = StatusEffects()

    //Boss bars
    @Suppress("unused")
    private val bossBarManager = BossBarManager(this)

    /**
     * Sends a packet to the players client session
     *
     * @param packet The packet to be sent
     */
    fun sendPacket(packet: Packet) {
        clientSession.sendPacket(packet)
    }

    /**
     * Disconnects the player from the server
     *
     * @param message The message to be shown on why they were disconnected
     */
    fun disconnect(message: Component) {
        clientSession.disconnect(message)
    }

    /**
     * Sends a message to the player
     *
     * @param message The message to be sent to the client
     */
    fun sendMessage(message: TextComponent) {
        sendPacket(ServerChatMessagePacket(message, ChatPosition.CHAT, null))
    }

    /**
     * Sets the time of day (clientside) for the player
     *
     * @param time The time to set it to
     */
    fun setTimeOfDay(time: Long) {
        world?.timeOfDay = time
        sendPacket(ServerTimeUpdatePacket(Bullet.world.worldAge, time))
    }

    /**
     * Sets the game mode for the player
     *
     * @param mode The game mode to set
     */
    fun setGameMode(mode: GameMode) {
        gameMode = mode
        sendPacket(ServerChangeGameStatePacket(3, mode.id.toFloat()))

        for(player in Bullet.players) {
            player.sendPacket(ServerPlayerInfoPacket(1, this))
        }
    }

    fun getHeldItem(): Int = inventory.getHeldItem(selectedSlot)

    fun setHeldItem(slot: Int) {
        selectedSlot = slot
        sendPacket(ServerHeldItemChangePacket(slot.toByte()))
    }

    /**
     * Sets the header of the tab list for all players
     * Make this empty to remove it
     *
     * @param header The header component to be displayed
     */
    fun setTabListHeader(header: Component) {
        val headerBuilder = Component.text().append(header)
        sendPacket(
            ServerPlayerListHeaderAndFooterPacket(headerBuilder.asComponent(), tabListFooter.asComponent())
        )

        tabListHeader = headerBuilder
    }

    /**
     * Sets the footer of the tab list for all players
     * Make this empty to remove it
     *
     * @param footer The footer component to be displayed
     */
    fun setTabListFooter(footer: Component) {
        val footerBuilder = Component.text().append(footer)
        sendPacket(
            ServerPlayerListHeaderAndFooterPacket(tabListHeader.asComponent(), footerBuilder.asComponent())
        )

        tabListFooter = footerBuilder
    }
}
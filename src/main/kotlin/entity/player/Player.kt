package com.aznos.entity.player

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.commands.CommandSource
import com.aznos.datatypes.LocationType
import com.aznos.datatypes.Slot
import com.aznos.entity.Entity
import com.aznos.entity.player.data.ChatPosition
import com.aznos.entity.player.data.GameMode
import com.aznos.entity.player.data.PermissionLevel
import com.aznos.packets.Packet
import com.aznos.entity.player.data.PlayerProperty
import com.aznos.packets.play.out.*
import com.aznos.world.World
import com.aznos.world.items.ItemStack
import com.aznos.world.sounds.SoundCategories
import com.aznos.world.sounds.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import java.util.UUID

/**
 * Represents a player in the game
 *
 * @property clientSession The client session associated with the player
 */
@Suppress("TooManyFunctions")
class Player(
    val clientSession: ClientSession
) : Entity(), CommandSource {
    override lateinit var username: String
    override lateinit var uuid: UUID
    lateinit var locale: String
    lateinit var brand: String
    var world: World? = Bullet.storage.getWorlds()[0] //TODO better player spawn world initialization

    //Inventory and Equipment
    var inventory = Inventory()
    var selectedSlot: Int = 0

    //Player attributes
    var properties = mutableListOf<PlayerProperty>()
    var gameMode: GameMode = GameMode.SURVIVAL
        private set
    var viewDistance: Int = 0
    var isSneaking: Boolean = false
    var ping: Int = 0
    var lastOnGroundY: Double = 0.0
    var fallDistance: Double = 0.0
    var isFlying: Boolean = false
    var canFly: Boolean = false
    var permissionLevel: PermissionLevel = PermissionLevel.MEMBER
    private var tabListHeader = Component.text()
    private var tabListFooter = Component.text()

    //Movement
    var onGround: Boolean = true
    var chunkX: Int = 0
    var chunkZ: Int = 0
    var lastSprintLocation: LocationType.Location? = null
    val loadedChunks = mutableSetOf<Pair<Int, Int>>()

    //Combat and status
    var status = StatusEffects()
    var experienceBar: Float = 0.0f
    var level: Int = 0
    var totalXP: Int = 0

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
    override fun sendMessage(message: TextComponent) {
        sendPacket(ServerChatMessagePacket(message, ChatPosition.CHAT, null))
    }

    /**
     * Sets the time of day (clientside) for the player
     *
     * @param time The time to set it to
     */
    fun setTimeOfDay(time: Long) {
        if(world == null) return

        world!!.timeOfDay = time
        sendPacket(ServerTimeUpdatePacket(world!!.worldAge, time))
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

    fun getHeldItem(): ItemStack = inventory.heldStack(selectedSlot)
    fun getHeldItemID(): Int = getHeldItem().id

    fun setHeldItem(slot: Int) {
        selectedSlot = slot
        sendPacket(ServerHeldItemChangePacket(slot.toByte()))
    }

    /**
     * Puts [stack] into the player's inventory at the specified absolute [index]
     *
     * `index` is the raw window-slot index that the client uses, which is:
     * (0-8 = crafting grid, 9-35 = player inventory, 36-44 = hotbar, 45 = offhand)
     */
    fun setSlot(index: Int, stack: ItemStack) {
        inventory.set(index, stack)
        sendPacket(ServerSetSlotPacket(0, index, stack.toSlotData()))
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

    /**
     * Sets a title (and optionally) a subtitle for the player
     *
     * @param title The main title to display, this is required
     * @param subtitle If you want to have a subtitle, put it here
     * @param fadeIn The time in ticks to fade in the title
     * @param stay The time in ticks to stay on the screen
     * @param fadeOut The time in ticks to fade out the title
     */
    fun setTitle(
        title: TextComponent,
        subtitle: TextComponent? = null,
        fadeIn: Int = 10,
        stay: Int = 80,
        fadeOut: Int = 20
    ) {
        sendPacket(ServerTitlePacket(
            TitleAction.SET_TIME_AND_DISPLAY,
            fadeInTicks = fadeIn,
            stayTicks = stay,
            fadeOutTicks = fadeOut)
        )

        sendPacket(ServerTitlePacket(TitleAction.SET_TITLE, title))
        if(subtitle != null) {
            sendPacket(ServerTitlePacket(TitleAction.SET_SUBTITLE, subtitle))
        }
    }

    /**
     * Sets the action bar text for the player
     *
     * @param text The text to display in the action bar
     */
    fun setActionBar(text: TextComponent) {
        sendPacket(ServerTitlePacket(TitleAction.SET_ACTIONBAR, text))
    }

    /**
     * Removes the title/subtitle from the screen, this can also be used to remove an action bar
     */
    fun removeTitle() {
        sendPacket(ServerTitlePacket(TitleAction.RESET))
    }

    /**
     * Sends a scoreboard to the player
     *
     * @param objectiveName The internal name of the scoreboard (should be unique)
     * @param displayName The display name for the scoreboard
     * @param lines The lines to be displayed on the scoreboard
     */
    fun sendScoreboard(objectiveName: String, displayName: TextComponent, lines: Map<String, Int>) {
        sendPacket(ServerScoreboardObjectivePacket(
            objectiveName,
            0,
            displayName,
            0
        ))

        sendPacket(ServerDisplayScoreboardPacket(1, objectiveName))
        for((lineText, score) in lines) {
            sendPacket(ServerUpdateScorePacket(
                lineText,
                0,
                objectiveName,
                score
            ))
        }
    }

    /**
     * Used to remove a scoreboard for the player
     *
     * @param objectiveName The internal name of the scoreboard
     */
    fun removeScoreboard(objectiveName: String) {
        sendPacket(ServerScoreboardObjectivePacket(objectiveName, 1))
    }

    /**
     * Plays a sound effect for the player at their location
     *
     * @param sound The sound to be played
     * @param categories The category under which the sound effect falls
     * @param volume Capped between 0.0f and 1.0f
     * @param pitch Float between 0.5 and 2.0
     */
    fun playSound(sound: Sounds, categories: SoundCategories, volume: Float, pitch: Float) {
        sendPacket(ServerEntitySoundEffectPacket(
            sound, categories,
            entityID,
            volume, pitch
        ))
    }

    /**
     * Adds an item to the inventory, searching first in the hotbar (slots 36-44) then the main inventory (slots 9-35).
     *
     * @param stack The ItemStack to add to the inventory
     * @return True if the item was added successfully, false if the inventory is full
     */
    fun addItem(stack: ItemStack): Boolean {
        for(slot in 36..44) {
            if(inventory.items[slot] == null || inventory.items[slot]?.isAir == true) {
                inventory.set(slot, stack)
                sendPacket(ServerSetSlotPacket(0, slot, stack.toSlotData()))
                return true
            }
        }

        for(slot in 9..35) {
            if(inventory.items[slot] == null || inventory.items[slot]?.isAir == true) {
                inventory.set(slot, stack)
                sendPacket(ServerSetSlotPacket(0, slot, stack.toSlotData()))
                return true
            }
        }

        return false
    }
}
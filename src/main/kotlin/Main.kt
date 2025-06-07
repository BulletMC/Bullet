package com.aznos

import com.aznos.events.EventManager
import com.aznos.events.PlayerJoinEvent
import com.aznos.packets.play.out.ServerSetSlotPacket
import com.aznos.world.items.Item
import com.aznos.world.items.ItemStack
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * Application entry point
 */
fun main(args: Array<String>) {
    val parser = ArgParser("bullet")

    val address by parser.option(
        ArgType.String,
        shortName = "a",
        fullName = "address",
        description = "Server address"
    ).default("0.0.0.0")

    val port by parser.option(
        ArgType.Int,
        shortName = "p",
        fullName = "port",
        description = "Server port"
    ).default(25565)

    parser.parse(args)

    EventManager.register(PlayerJoinEvent::class.java) { e ->
        val player = e.player
        val inv = player.inventory

        val sword = ItemStack(
            item = Item.DIAMOND_SWORD,
            count = 1,
            displayName = MiniMessage.miniMessage().deserialize("<yellow>Blade</yellow>"),
            lore = listOf(
                Component.text("A sharp blade..", NamedTextColor.GRAY),
                Component.text("this item will kill", NamedTextColor.GRAY)
            )
        )

        val hotbarSlot = 0
        val windowSlotID = 36 + hotbarSlot

        inv.set(windowSlotID, sword)
        player.sendPacket(ServerSetSlotPacket(
            0,
            windowSlotID,
            sword.toSlotData()
        ))
    }

    Bullet.createServer(address, port)
}

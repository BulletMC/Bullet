package com.aznos.packets.play.`in`

import com.aznos.Bullet
import com.aznos.ClientSession
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandManager
import com.aznos.datatypes.StringType.readString
import com.aznos.events.EventManager
import com.aznos.events.PlayerChatEvent
import com.aznos.packets.Packet
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * This packet is sent to the server whenever the client sends a chat message
 *
 * @property message The message the client sent
 */
class ClientChatMessagePacket(data: ByteArray) : Packet(data) {
    val message: String = getIStream().readString()

    override fun apply(client: ClientSession) {
        if(message.length > 255) {
            client.player.sendMessage(
                Component.text("Message is too long")
                    .color(NamedTextColor.RED)
            )

            return
        }

        if(message.startsWith('/') && message.length > 1) {
            val command = message.substring(1)
            val commandSource = client.player

            @Suppress("TooGenericExceptionCaught")
            val result: Int = try {
                CommandManager.dispatcher.execute(command, commandSource)
            } catch(e: CommandSyntaxException) {
                CommandCodes.ILLEGAL_SYNTAX.id
            } catch (e: Exception) {
                Bullet.logger.warn("Error running command `$message`:", e)
                return
            }

            if(result == CommandCodes.SUCCESS.id) return

            when(result) {
                CommandCodes.UNKNOWN.id ->
                    commandSource.sendMessage(
                        Component.text("Unknown command")
                            .color(NamedTextColor.RED)
                    )

                CommandCodes.ILLEGAL_ARGUMENT.id,
                CommandCodes.ILLEGAL_SYNTAX.id ->
                    commandSource.sendMessage(
                        Component.text("Invalid command syntax, try typing /help")
                            .color(NamedTextColor.RED)
                    )

                CommandCodes.INVALID_PERMISSIONS.id ->
                    commandSource.sendMessage(
                        Component.text("You don't have permission to use this command")
                            .color(NamedTextColor.RED)
                    )
            }
            return
        }

        val formattedMessage = message.replace('&', '§')

        val event = PlayerChatEvent(client.player, formattedMessage)
        EventManager.fire(event)
        if(event.isCancelled) return

        val textComponent = Component.text()
            .append(Component.text().content("<").color(NamedTextColor.GRAY))
            .append(Component.text().content(client.player.username).color(TextColor.color(0x55FFFF)))
            .append(Component.text().content("> ").color(NamedTextColor.GRAY))
            .append(MiniMessage.miniMessage().deserialize(formattedMessage))
            .build()

        Bullet.broadcast(textComponent)
    }
}
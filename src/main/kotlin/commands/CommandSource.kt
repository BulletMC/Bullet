package com.aznos.commands

import net.kyori.adventure.text.TextComponent

interface CommandSource {
    val username: String
    fun sendMessage(message: TextComponent)
}
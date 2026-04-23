package com.maddoxh.bullet.network.chat

import com.google.gson.JsonObject

object ChatComponent {
    fun literal(text: String): String {
        val obj = JsonObject()
        obj.addProperty("text", text)
        return obj.toString()
    }

    fun colored(text: String): String = literal(text)
}
package com.aznos.commands.data

import com.aznos.packets.newPacket.ResourceLocation
import java.io.DataInputStream

object Parsers {

    val parsers = mutableListOf<Parser>()
    val parserMap = mutableMapOf<String, Parser>()

    fun define(id: String ,writer: (DataInputStream, List<Any>) -> Unit) {

    }

    class Parser(
        val id: Int,
        val name: ResourceLocation
    )

}
package com.aznos.api

interface Plugin {
    fun onEnable()
    fun onDisable()
    fun getName(): String
    fun registerEvents() {}
    fun registerCommands() {}
}
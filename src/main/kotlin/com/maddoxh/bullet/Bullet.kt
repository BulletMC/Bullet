package com.maddoxh.bullet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.logging.log4j.LogManager
import org.slf4j.Logger
import java.net.ServerSocket

class Bullet(private val port: Int = 25565) {
    private val logger = LogManager.getLogger()

    suspend fun start() = coroutineScope {
        val serverSocket = ServerSocket(port)
        logger.info("Listening on port $port")

        while(true) {
            val socket = withContext(Dispatchers.IO) { serverSocket.accept() }
            launch {
                println("Server socket accepted")
            }
        }
    }
}
package com.maddoxh.bullet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.ServerSocket

class Bullet(val port: Int = 25565) {
    var motd: String = "A Bullet Server"
    var maxPlayers: Int = 100
    var onlinePlayers: Int = 0
    var versionName: String = "BulletMC 1.21.11"
    var protocolVersion: Int = 774
    var enforcesSecureChat: Boolean = false

    suspend fun start() = coroutineScope {
        val serverSocket = ServerSocket(port)
        logger.info("Listening on port $port")

        while(true) {
            val socket = withContext(Dispatchers.IO) { serverSocket.accept() }
            launch {
                ClientConnection(socket, this@Bullet).handle()
            }
        }
    }

    companion object {
        val logger: Logger = LogManager.getLogger()
    }
}

package com.maddoxh.bullet

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

data class Address(val host: String, val port: Int)
class Bullet(val address: Address = Address("0.0.0.0", 25565)) {
    val logger: Logger = LogManager.getLogger(Bullet::class.java)

    var server: ServerSocket? = null
    var client: Socket? = null

    fun createServer() = runBlocking {
        val selector = SelectorManager(Dispatchers.IO)
        server = aSocket(selector).tcp().bind(hostname = address.host, port = address.port)
        logger.info("Bullet server started on ${address.host}:${address.port}")

        while(true) {
            client = server!!.accept()
            logger.info("Client connected: ${client!!.remoteAddress}")
        }
    }

    fun stopServer() {
        client?.close()
        server?.close()
    }
}
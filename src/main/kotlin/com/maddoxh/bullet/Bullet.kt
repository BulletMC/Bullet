package com.maddoxh.bullet

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Bullet(val port: Int) {
    val logger: Logger = LogManager.getLogger(Bullet::class.java)

    var server: ServerSocket? = null
    var client: Socket? = null

    fun createServer() {
        server = ServerSocket(port)
        logger.info("Bullet server started on port $port")

        while(true) {
            client = server!!.accept()
            logger.info("Client connected: ${client!!.inetAddress.hostAddress}")
        }
    }

    fun stopServer() {
        client?.close()
        server?.close()
    }
}
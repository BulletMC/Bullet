package com.aznos

import com.aznos.commands.CommandManager
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.Position
import com.aznos.world.data.TimeOfDay
import com.google.gson.JsonParser
import dev.dewy.nbt.api.registry.TagTypeRegistry
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlinx.coroutines.*
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.io.InputStreamReader
import java.net.BindException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.Base64
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.time.Duration.Companion.seconds

/**
 * This is where the core of the bullet server logic will be housed
 */
object Bullet : AutoCloseable {
    const val PROTOCOL: Int = 754 // Protocol version 769 = Minecraft version 1.16.5
    const val VERSION: String = "1.16.5"

    @Suppress("MaxLineLength")
    var favicon: String = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAexJREFUeJztm0GOgjAUht8I0WAmuuFGnsPNLJx4iFlNMlcwmYUbr+cGYqgQcWZhMLS2ULD1J+F9CQmR+vjfZ4GGxLfVavVHIyYkIjoej+gcEOI4pgk6BBoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgYQHoAGhYADoAGhaADoCGBaADoAmrnTRNabFY3PeHSJXPJaHpwOF76fxkfVh/JXTY3rKsd4l0zIUQo4AhUomocCFEEuB76meiICKieTR1Us+FkJfOAFeNm2gSYpLhRUAmCu/N2lAXst4lWgm9HoOZKKRNpWvzuhpNiLwkkZedxhy2S+0l3kvAPJpKWxtNsqp6XYhmAUWzwHhc5KV2jE7CSxZCTbKaxPTFJEfkJf1+vEsS4I9B3/eKtkullwCb614d47NRtcloFhgb3+xP0s3QKKD+zLaZopko6FJeHz4Pg8lDPZtaddp+RRXb5oksZkB6Ot+baEM3TpXSVu9SXq3P1wVd80QWAp4N0/X7Lpvf7E/3/ZcuhFDUGyYa4FLYNX0aVpEEDP19gIuGVYwz4PNHPF3cFVXjXl+I1Iv7ONFQGf07QRaADoCGBaADoGEB6ABoWAA6ABoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgCYlu/6IeK/8hQ6uwCcyPRQAAAABJRU5ErkJggg=="
    var max_players: Int = 20
    var motd: String = "§6Runs as fast as a bullet"

    val logger: Logger = LogManager.getLogger()

    private val pool = Executors.newCachedThreadPool()
    private var server: ServerSocket? = null
    val players = mutableListOf<Player>()

    val breakingBlocks = mutableMapOf<Position, Job>()

    var worldAge = 0L
    var timeOfDay: Long = TimeOfDay.SUNRISE.time

    var dimensionCodec: CompoundTag? = null

    /**
     * Creates and runs the server instance
     *
     * @param host - The IP address of the server, for local development set this to 0.0.0.0
     * @param port - The port the server will run on, this defaults at 25565
     */
    fun createServer(host: String, port: Int = 25565) {
        try {
            server = ServerSocket().apply {
                bind(InetSocketAddress(host, port))
            }
        } catch(e: BindException) {
            logger.error("Failed to bind to $host:$port, is the address already in use?")
            return
        }

        val reader = javaClass.getResourceAsStream("/codec.json")?.let {
            InputStreamReader(it)
        }

        val parsed = JsonParser.parseReader(reader).asJsonObject
        dimensionCodec = CompoundTag().fromJson(parsed, 0, TagTypeRegistry())

        CommandManager.registerCommands()
        scheduleTimeUpdate()

        logger.info("Bullet server started at $host:$port")

        while(!isClosed()) {
            val client = server?.accept()

            pool.submit {
                client?.let {
                    ClientSession(it).handle()
                }
            }
        }
    }

    /**
     * Schedules a coroutine to update the time of day every second
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun scheduleTimeUpdate() {
        GlobalScope.launch {
            coroutineScope {
                while(isActive) {
                    delay(1.seconds)

                    timeOfDay = (timeOfDay + 20) % 24000
                    worldAge += 20

                    for(player in players) {
                        player.setTimeOfDay(timeOfDay)
                    }
                }
            }
        }
    }

    /**
     * Sets the favicon for the server from a PNG located at the specified resource path and encodes it to base64
     * This must be called before the server is started
     *
     * @param resourcePath The path to the PNG file resource
     */
    fun setFaviconFromPNG(resourcePath: String) {
        try {
            val stream = javaClass.getResourceAsStream(resourcePath)
            if(stream == null) {
                logger.error("Favicon resource not found at: $resourcePath")
                return
            }

            val imageBytes = stream.readBytes()
            val base64String = Base64.getEncoder().encodeToString(imageBytes)

            favicon = "data:image/png;base64,$base64String"
        } catch(e: IOException) {
            logger.error("Failed to read favicon resource at: $resourcePath")
        } catch(e: IllegalArgumentException) {
            logger.error("Failed to encode favicon resource to base64")
        }
    }

    /**
     * Broadcasts a message to all players on the server
     *
     * @param message The message to be sent to all players, this supports mini message format
     */
    fun broadcast(message: String) {
        val translatedMessage: TextComponent = MiniMessage.miniMessage().deserialize(message) as TextComponent

        for(player in players) {
            player.sendMessage(translatedMessage)
        }
    }

    /**
     * Broadcasts a message to all players on the server
     *
     * @param message The message to be sent to all players
     */
    fun broadcast(message: TextComponent) {
        for(player in players) {
            player.sendMessage(message)
        }
    }

    /**
     * Returns if the server instance is either closed or it is not bound
     *
     * @return True if the server is closed
     */
    private fun isClosed() : Boolean {
        return server?.let {
            it.isClosed || !it.isBound
        } ?: true
    }

    /**
     * Shuts down the server
     */
    override fun close() {
        for(player in players) {
            player.disconnect("Server is shutting down")
        }

        server?.close()
        pool.shutdown()
    }
}
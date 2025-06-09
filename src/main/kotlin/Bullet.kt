package com.aznos

import com.aznos.api.Plugin
import com.aznos.api.PluginMetadata
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandManager
import com.aznos.datatypes.BlockPositionType
import com.aznos.entity.ConsoleSender
import com.aznos.entity.player.Player
import com.aznos.packets.play.out.ServerParticlePacket
import com.aznos.storage.EmptyStorage
import com.aznos.storage.StorageManager
import com.aznos.storage.disk.DiskServerStorage
import com.aznos.world.data.Particles
import com.google.gson.Gson
import com.google.gson.JsonParser
import dev.dewy.nbt.api.registry.TagTypeRegistry
import dev.dewy.nbt.tags.collection.CompoundTag
import kotlinx.coroutines.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.BindException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.URLClassLoader
import java.security.KeyPairGenerator
import java.util.Base64
import java.util.concurrent.Executors
import java.util.jar.JarFile
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * This is where the core of the bullet server logic is housed
 */
@Suppress("TooManyFunctions")
object Bullet : AutoCloseable {
    const val PROTOCOL: Int = 754 // Protocol version 769 = Minecraft version 1.16.5
    const val VERSION: String = "1.16.5"
    const val BULLET_VERSION: String = "PREALPHA-0.0.1"

    @Suppress("MaxLineLength")
    var favicon: String = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAexJREFUeJztm0GOgjAUht8I0WAmuuFGnsPNLJx4iFlNMlcwmYUbr+cGYqgQcWZhMLS2ULD1J+F9CQmR+vjfZ4GGxLfVavVHIyYkIjoej+gcEOI4pgk6BBoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgYQHoAGhYADoAGhaADoCGBaADoAmrnTRNabFY3PeHSJXPJaHpwOF76fxkfVh/JXTY3rKsd4l0zIUQo4AhUomocCFEEuB76meiICKieTR1Us+FkJfOAFeNm2gSYpLhRUAmCu/N2lAXst4lWgm9HoOZKKRNpWvzuhpNiLwkkZedxhy2S+0l3kvAPJpKWxtNsqp6XYhmAUWzwHhc5KV2jE7CSxZCTbKaxPTFJEfkJf1+vEsS4I9B3/eKtkullwCb614d47NRtcloFhgb3+xP0s3QKKD+zLaZopko6FJeHz4Pg8lDPZtaddp+RRXb5oksZkB6Ot+baEM3TpXSVu9SXq3P1wVd80QWAp4N0/X7Lpvf7E/3/ZcuhFDUGyYa4FLYNX0aVpEEDP19gIuGVYwz4PNHPF3cFVXjXl+I1Iv7ONFQGf07QRaADoCGBaADoGEB6ABoWAA6ABoWgA6AhgWgA6BhAegAaFgAOgAaFoAOgIYFoAOgCYlu/6IeK/8hQ6uwCcyPRQAAAABJRU5ErkJggg=="
    var max_players: Int = 20
    var motd: String = "§6Runs as fast as a bullet!"

    val logger: Logger = LogManager.getLogger(Bullet::class.java)

    private val pool = Executors.newCachedThreadPool()
    private var server: ServerSocket? = null
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var shouldPersist by Delegates.notNull<Boolean>()
    var onlineMode by Delegates.notNull<Boolean>()
    var publicKey: ByteArray? = null

    val loadedPlugins = mutableListOf<Plugin>()
    val players = mutableListOf<Player>()

    lateinit var storage: StorageManager
    val startupTime = System.currentTimeMillis()

    val breakingBlocks = mutableMapOf<BlockPositionType.BlockPosition, Job>()
    val sprinting = mutableSetOf<Int>()

    var dimensionCodec: CompoundTag? = null

    /**
     * Creates and runs the server instance
     *
     * @param host - The IP address of the server for local development set this to 0.0.0.0
     * @param port - The port the server will run on, this defaults at 25565
     * @param onlineMode - If set to false, the server will not check if players are
     * authenticated with a microsoft account.
     * This means anyone with the same username can have access to the same account, and skins will not be loaded
     * @param shouldPersist - If set to true, the server will save world data and block data to disk
     * for persistent storage, if set to false, nothing will save when the server is restarted
     */
    fun createServer(host: String, port: Int = 25565, onlineMode: Boolean = true, shouldPersist: Boolean = true) {
        this.onlineMode = onlineMode
        this.shouldPersist = shouldPersist

        generatePublicKey()

        storage = StorageManager(
            if(shouldPersist) DiskServerStorage()
            else EmptyStorage())

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

        loadPlugins()
        CommandManager.registerCommands()

        // Load world(s)
        storage.getOrLoadWorld("world")

        scheduleTimeUpdate()
        scheduleSprintingParticles()
        if(shouldPersist) {
            scheduleSaveUpdate()
        }

        logger.info("Bullet server started at $host:$port")
        launchConsole()

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
     * Generates the 1024-bit RSA public key for the server to be used for encryption
     */
    @Suppress("TooGenericExceptionCaught")
    private fun generatePublicKey() {
        try {
            val keyPair = KeyPairGenerator.getInstance("RSA").apply {
                initialize(1024)
            }.generateKeyPair()

            publicKey = keyPair.public.encoded
        } catch(e: Exception) {
            logger.error("Failed to generate public key for the server", e)
        }
    }

    /**
     * Loads all plugins from inside the /plugins folder
     */
    private fun loadPlugins() {
        val pluginDir = File("plugins")
        if(!pluginDir.exists()) pluginDir.mkdirs()

        pluginDir.listFiles { file -> file.extension == "jar" }?.forEach { file ->
            val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()), this::class.java.classLoader)
            val jar = JarFile(file)

            val pluginJsonEntry = jar.getEntry("plugin.json")
            if(pluginJsonEntry == null) {
                logger.warn("Plugin ${file.name} does not contain a plugin.json file, skipping...")
                return@forEach
            }

            val pluginJson = jar.getInputStream(pluginJsonEntry).reader().readText()
            val metadata = parsePluginJson(pluginJson)

            if(metadata.bulletVersion != BULLET_VERSION) {
                logger.warn(
                    "Plugin ${metadata.name} was built for BulletMC ${metadata.bulletVersion}, " +
                    "but the server is running $BULLET_VERSION, this may cause issues"
                )
            }

            val pluginClass = classLoader.loadClass(metadata.mainClass)
            if(Plugin::class.java.isAssignableFrom(pluginClass) && !pluginClass.isInterface) {
                val plugin = pluginClass.getDeclaredConstructor().newInstance() as Plugin

                plugin.onEnable()
                plugin.registerEvents()
                plugin.registerCommands()

                loadedPlugins.add(plugin)
            }
        }
    }

    private fun parsePluginJson(json: String): PluginMetadata {
        val parser = Gson()
        return parser.fromJson(json, PluginMetadata::class.java)
    }

    /**
     * Schedules a coroutine to update the time of day every second
     */
    private fun scheduleTimeUpdate() {
        scope.launch {
            while(isActive) {
                delay(1.seconds)

                for (world in storage.getWorlds()) {
                    world.timeOfDay = (world.timeOfDay + 20) % 24000
                    world.worldAge += 20
                }
            }
        }
    }

    /**
     * Every 5 seconds the server will save the world data and block data to disk for persistent storage
     */
    private fun scheduleSaveUpdate() {
        if(shouldPersist) {
            scope.launch {
                while(isActive) {
                    delay(5.seconds)
                    for (world in storage.getWorlds()) {
                        world.save()
                    }
                }
            }
        }
    }

    /**
     * Schedules a coroutine to send particles to players who are sprinting so that other players can see them
     */
    private fun scheduleSprintingParticles() {
        scope.launch {
            while(isActive) {
                for(entityID in sprinting) {
                    val player = players.find { it.entityID == entityID } ?: continue

                    val x = player.location.x
                    val y = player.location.y + 0.1f
                    val z = player.location.z

                    for(otherPlayer in players) {
                        if(otherPlayer != player) {
                            otherPlayer.clientSession.sendPacket(ServerParticlePacket(
                                Particles.Block(1),
                                false,
                                BlockPositionType.BlockPosition(x, y, z),
                                0.001f,
                                0.0005f,
                                0.001f,
                                0.0f,
                                5
                            ))
                        }
                    }
                }

                delay(200.milliseconds)
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun launchConsole() {
        scope.launch(Dispatchers.IO) {
            val reader = System.`in`.bufferedReader()
            while(isActive) {
                val input = reader.readLine() ?: continue

                try {
                    val res = CommandManager.dispatcher.execute(input, ConsoleSender)

                    when(res) {
                        CommandCodes.ILLEGAL_ARGUMENT.id ->
                            ConsoleSender.sendMessage(Component.text("Illegal argument", NamedTextColor.RED))
                        CommandCodes.INVALID_PERMISSIONS.id -> {
                            ConsoleSender.sendMessage(Component.text(
                                "You do not have permission to run this command", NamedTextColor.RED)
                            )
                        }
                        CommandCodes.ILLEGAL_SYNTAX.id ->
                            ConsoleSender.sendMessage(Component.text("Illegal syntax", NamedTextColor.RED))
                        CommandCodes.UNKNOWN.id ->
                            ConsoleSender.sendMessage(Component.text("Unknown command", NamedTextColor.RED))
                    }
                } catch(e: Exception) {
                    logger.warn("[Console Error] ${e.message}", e)
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
    @Suppress("unused")
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
        for(plugin in loadedPlugins) {
            try {
                plugin.onDisable()
            } catch(e: IOException) {
                logger.error("Failed to disable plugin ${plugin.getName()}", e)
            }
        }

        for(world in storage.getWorlds()) {
            world.save()
        }

        for(player in ArrayList(players)) {
            player.disconnect(Component.text("Server is shutting down"))
        }

        scope.cancel()
        server?.close()
        pool.shutdown()
    }
}
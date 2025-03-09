import com.google.gson.GsonBuilder
import util.DataFetcher
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI
import java.nio.channels.Channels
import java.util.logging.Logger
import java.util.zip.ZipInputStream

object Main {

    const val VERSION = "1.21.4"
    private const val SKIP_DOWNLOADING = true

    val logger = Logger.getLogger("Bullet Code Generator")
    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    val buildFolder = File("build/")
    val assetsFolder = buildFolder.resolve("minecraft-assets-$VERSION")
    val dataFolder = buildFolder.resolve("minecraft-data-master")

    init {
        if (!SKIP_DOWNLOADING) {
            if (buildFolder.exists()) buildFolder.deleteRecursively()
            buildFolder.mkdirs()

            logger.info("Downloading data...")
            val assetsZip = downloadFile("https://github.com/InventivetalentDev/minecraft-assets/archive/refs/heads/$VERSION.zip")
            val dataZip = downloadFile("https://github.com/PrismarineJS/minecraft-data/archive/refs/heads/master.zip")

            logger.info("Extracting data...")
            extractMinecraftDataZip(assetsZip, buildFolder)
            extractMinecraftDataZip(dataZip, buildFolder)

            assetsZip.deleteOnExit()
            dataZip.deleteOnExit()
        } else {
            logger.warning("Skipped downloading data")

            if (!dataFolder.resolve("data/dataPaths.json").exists()) {
                throw RuntimeException("Cannot skip downloading data, because no data exists!")
            }
        }

        logger.info("Initializing...")
        DataFetcher.initialize()
    }

    fun initialize() {}

    private fun downloadFile(url: String): File {
        val zipFile = File.createTempFile("download", ".zip")

        val channel = Channels.newChannel(URI(url).toURL().openStream())
        val outputStream = FileOutputStream(zipFile)
        outputStream.channel.transferFrom(channel, 0, Long.MAX_VALUE)
        outputStream.close()
        channel.close()

        return zipFile
    }

    private fun extractMinecraftDataZip(zipFile: File, outputFolder: File) {
        val zis = ZipInputStream(FileInputStream(zipFile))

        var entry = zis.nextEntry
        val buffer = ByteArray(1024)

        while (entry != null) {
            val file = outputFolder.resolve(entry.name)

            if (entry.isDirectory) {
                file.mkdirs()
            } else {
                file.parentFile.mkdirs()
                file.createNewFile()

                val outputStream = FileOutputStream(file)
                var len: Int
                while ((zis.read(buffer).also { len = it }) > 0) {
                    outputStream.write(buffer, 0, len)
                }
            }

            zis.closeEntry()
            entry = zis.nextEntry
        }
    }

}

fun main() = Main.initialize()
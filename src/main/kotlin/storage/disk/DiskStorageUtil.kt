package com.aznos.storage.disk

import com.aznos.Bullet
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files

object DiskStorageUtil {

    val json = Json { allowStructuredMapKeys = true }

    /**
     * Deserialize file and use default if could not read it
     *
     * @param T The type of the object to deserialize
     * @param file The file to read the object from
     * @param default The used value if the file content is empty or an error happens
     * @return The deserialized data if could deserialize, "default" otherwise
     */
    inline fun <reified T> readFileData(file: File, default: T): T {
        if (!file.exists()) return default

        val jsonData = Files.readString(file.toPath())
        if (jsonData.isEmpty()) {
            Bullet.logger.warn("Data of file ${file.path} is empty")
            return default
        }

        return try {
            json.decodeFromString<T>(jsonData)
        } catch (e: SerializationException) {
            Bullet.logger.error("Could not read file ${file.path}", e)
            default
        } catch (e: IllegalArgumentException) {
            Bullet.logger.error("Could not read file ${file.path}", e)
            default
        }
    }

    /**
     * Writes serialized data to the file
     *
     * @param file The file to save the data to
     * @param data The data to save
     * @return If the operation was successful
     */
    inline fun <reified T> writeFileData(file: File, data: T): Boolean {
        file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()

        val jsonData = json.encodeToString(data)
        Files.write(file.toPath(), jsonData.toByteArray())
        return true
    }

}
package com.aznos.datatypes

import kotlinx.serialization.Serializable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationType {
    /**
     * Represents a 3 dimensional vector
     */
    data class Vector3(val x: Double, val y: Double, val z: Double) {
        fun normalize(): Vector3 {
            val length = sqrt(x * x + y * y + z * z)
            return if(length == 0.0) this else Vector3(x / length, y / length, z / length)
        }
    }

    /**
     * Represents a location in the world, see [BlockPosition] for block positions
     */
    @Serializable
    data class Location(
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    ) {

        constructor(x: Double, y: Double, z: Double) : this(x, y, z, 0f, 0f)

        fun add(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location {
            return copy(
                x = this.x + x,
                y = this.y + y,
                z = this.z + z,
                yaw = this.yaw + yaw,
                pitch = this.pitch + pitch
            )
        }

        fun add(x: Double, y: Double, z: Double): Location {
            return copy(
                x = this.x + x,
                y = this.y + y,
                z = this.z + z,
            )
        }

        fun add(yaw: Float, pitch: Float): Location {
            return copy(
                yaw = this.yaw + yaw,
                pitch = this.pitch + pitch
            )
        }

        fun set(x: Double, y: Double, z: Double): Location {
            return copy(
                x = x,
                y = y,
                z = z
            )
        }

        fun set(yaw: Float, pitch: Float): Location {
            return copy(
                yaw = yaw,
                pitch = pitch
            )
        }

        fun directionVector(): Vector3 {
            val yawRad = Math.toRadians(yaw.toDouble())
            val pitchRad = Math.toRadians(pitch.toDouble())

            val x = -sin(yawRad) * cos(pitchRad)
            val y = -sin(pitchRad)
            val z = cos(yawRad) * cos(pitchRad)

            return Vector3(x, y, z)
        }
    }


    /**
     * Reads a location from the [DataInputStream]
     *
     * @return Decoded location
     * @throws IOException If an I/O error occurs while reading the input stream
     */
    @Throws(IOException::class)
    fun DataInputStream.readLocation(): Location {
        val x = readDouble()
        val y = readDouble()
        val z = readDouble()
        val yaw = readFloat()
        val pitch = readFloat()

        return Location(x, y, z, yaw, pitch)
    }

    /**
     * Writes a Location to the [DataOutputStream] and modifies the yaw and pitch to be an angle
     *
     * @param location Location to write
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeLocationAngle(location: Location) {
        writeDouble(location.x)
        writeDouble(location.y)
        writeDouble(location.z)
        writeByte((location.yaw * 256.0f / 360.0f).toInt())
        writeByte((location.pitch * 256.0f / 360.0f).toInt())
    }

    /**
     * Writes a Location to the [DataOutputStream]
     *
     * @param location Location to write
     * @throws IOException If an I/O error occurs while writing to the output stream
     */
    @Throws(IOException::class)
    fun DataOutputStream.writeLocation(location: Location) {
        writeDouble(location.x)
        writeDouble(location.y)
        writeDouble(location.z)
        writeFloat(location.yaw)
        writeFloat(location.pitch)
    }
}
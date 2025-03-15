package com.aznos

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.Socket

/**
 * Application entry point
 */
fun main() {
    Bullet.createServer("0.0.0.0")
}
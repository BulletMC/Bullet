package com.maddoxh.bullet

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val bullet = Bullet()
    bullet.motd = "§6BulletMC §7- as fast as a Bullet"
    bullet.maxPlayers = 67

    bullet.start()
}

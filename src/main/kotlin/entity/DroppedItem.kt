package com.aznos.entity

class DroppedItem : Entity() {
    val spawnTimeMs: Long = System.currentTimeMillis()
    val pickupDelayMs: Long = 500
}
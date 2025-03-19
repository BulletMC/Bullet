package com.aznos.entity

import java.util.UUID

open class Entity {
    val entityID: Int = lastID++
    open val uuid: UUID = UUID.randomUUID()

    companion object {
        private var lastID = 0
    }
}
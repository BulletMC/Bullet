package com.aznos.entity.livingentity

import com.aznos.entity.Entity
import java.util.UUID

class LivingEntity : Entity() {
    val uuid: UUID = UUID.randomUUID()
}
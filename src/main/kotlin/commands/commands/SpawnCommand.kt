package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.Entity
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.player.Player
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component

class SpawnCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("spawn")
                .then(
                    RequiredArgumentBuilder.argument<Player, Int>("type", IntegerArgumentType.integer())
                        .executes { context ->
                            val entityType = IntegerArgumentType.getInteger(context, "type")
                            val entity = LivingEntity()

                            for(player in Bullet.players) {
                                player.sendPacket(ServerSpawnLivingEntityPacket(
                                    entity.entityID,
                                    entity.uuid,
                                    entityType,
                                    context.source.location,
                                    90f,
                                    0,
                                    0,
                                    0,
                                ))
                            }

                            Bullet.livingEntities.add(entity)
                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }
}
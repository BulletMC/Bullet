package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.Entity
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.player.Player
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SpawnCommand {
    fun register(dispatcher: CommandDispatcher<Player>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<Player>("spawn")
                .then(
                    RequiredArgumentBuilder.argument<Player, String>("type", StringArgumentType.word())
                        .executes { context ->
                            val type = StringArgumentType.getString(context, "type")
                            var entityType: LivingEntities? = null

                            for(entity in LivingEntities.entries) {
                                if(type == entity.name) {
                                    entityType = entity
                                    break
                                }
                            }

                            if(entityType == null) {
                                context.source.sendMessage(Component.text()
                                    .append(Component.text("Invalid entity type: ").color(NamedTextColor.RED))
                                    .append(Component.text(type).color(NamedTextColor.WHITE))
                                    .build())
                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            val entity = LivingEntity()

                            for(player in Bullet.players) {
                                player.sendPacket(ServerSpawnLivingEntityPacket(
                                    entity.entityID,
                                    entity.uuid,
                                    entityType.id,
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
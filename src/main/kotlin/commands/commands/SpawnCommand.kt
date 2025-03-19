package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.entity.Entity
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.nonliving.Entities
import com.aznos.entity.player.Player
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.mojang.brigadier.CommandDispatcher
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

                            val livingEntityType = findLivingEntityType(type)
                            val nonLivingEntityType = findNonLivingEntityType(type)

                            if(livingEntityType == null && nonLivingEntityType == null) {
                                sendInvalidEntityMessage(context.source, type)
                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            spawnEntity(context.source, livingEntityType, nonLivingEntityType)
                            CommandCodes.SUCCESS.id
                        }
                )
        )
    }

    private fun findLivingEntityType(type: String): LivingEntities? {
        return LivingEntities.entries.find { it.name.equals(type, ignoreCase = true) }
    }

    private fun findNonLivingEntityType(type: String): Entities? {
        return Entities.entries.find { it.name.equals(type, ignoreCase = true) }
    }

    private fun spawnEntity(player: Player, livingEntityType: LivingEntities?, nonLivingEntityType: Entities?) {
        if(livingEntityType != null) {
            val entity = LivingEntity()
            Bullet.players.forEach {
                it.sendPacket(
                    ServerSpawnLivingEntityPacket(
                        entity.entityID, entity.uuid, livingEntityType.id, player.location, 90f, 0, 0, 0
                    )
                )
            }

            Bullet.livingEntities.add(entity)
        } else if(nonLivingEntityType != null) {
            val entity = Entity()
            Bullet.players.forEach {
                it.sendPacket(
                    ServerSpawnEntityPacket(
                        entity.entityID, entity.uuid, nonLivingEntityType.id, player.location, 0, 0, 0, 0
                    )
                )
            }

            Bullet.entities.add(entity)
        }
    }

    private fun sendInvalidEntityMessage(player: Player, type: String) {
        player.sendMessage(
            Component.text()
                .append(Component.text("Invalid entity type: ").color(NamedTextColor.RED))
                .append(Component.text(type).color(NamedTextColor.YELLOW))
                .build()
        )
    }
}
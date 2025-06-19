package com.aznos.commands.commands

import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandSource
import com.aznos.commands.CommandManager
import com.aznos.entity.Entity
import com.aznos.entity.livingentity.LivingEntities
import com.aznos.entity.livingentity.LivingEntity
import com.aznos.entity.nonliving.Entities
import com.aznos.entity.player.Player
import com.aznos.entity.player.data.PermissionLevel
import com.aznos.packets.play.out.ServerSpawnEntityPacket
import com.aznos.packets.play.out.ServerSpawnLivingEntityPacket
import com.aznos.world.data.Difficulty
import com.aznos.world.data.EntityData
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.util.concurrent.CompletableFuture

class SpawnCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("spawn")
                .then(
                    RequiredArgumentBuilder.argument<CommandSource, String>("type", StringArgumentType.word())
                        .suggests(entityTypeSuggestions())
                        .executes { context ->
                            val sender = context.source
                            if(!CommandManager.hasModPermission(sender)) {
                                return@executes CommandCodes.INVALID_PERMISSIONS.id
                            }

                            val type = StringArgumentType.getString(context, "type")

                            val livingEntityType = findLivingEntityType(type)
                            val nonLivingEntityType = findNonLivingEntityType(type)

                            if(livingEntityType == null && nonLivingEntityType == null) {
                                sendInvalidEntityMessage(context.source as Player, type)
                                return@executes CommandCodes.ILLEGAL_ARGUMENT.id
                            }

                            spawnEntity(context.source as Player, livingEntityType, nonLivingEntityType)
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
            entity.location = player.location

            val entityData = EntityData(
                entity.uuid,
                player.location,
                livingEntityType.id,
                20,
                90f,
                0,
                0,
                0,
                true
            )

            Bullet.players.forEach {
                it.sendPacket(
                    ServerSpawnLivingEntityPacket(
                        entity.entityID, entity.uuid, livingEntityType.id, player.location, 90f, 0, 0, 0
                    )
                )
            }

            player.world!!.livingEntities.add(Pair(entity, entityData))
        } else if(nonLivingEntityType != null) {
            val entity = Entity()
            entity.location = player.location
            val entityData = EntityData(
                entity.uuid,
                player.location,
                nonLivingEntityType.id
            )

            Bullet.players.forEach {
                it.sendPacket(
                    ServerSpawnEntityPacket(
                        entity.entityID, entity.uuid, nonLivingEntityType.id, player.location, 0, 0, 0, 0
                    )
                )
            }

            player.world!!.entities.add(Pair(entity, entityData))
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

    private fun entityTypeSuggestions(): SuggestionProvider<CommandSource> {
        return SuggestionProvider { context, builder ->
            LivingEntities.entries.forEach {
                builder.suggest(it.name.lowercase())
            }

            Entities.entries.forEach {
                builder.suggest(it.name.lowercase())
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
package com.aznos.commands.commands.suggestions

import com.aznos.Bullet
import com.aznos.entity.player.Player
import com.mojang.brigadier.suggestion.SuggestionProvider
import java.util.concurrent.CompletableFuture

object PlayerSuggestions {
    fun playerNameSuggestions(): SuggestionProvider<Player> {
        return SuggestionProvider { context, builder ->
            Bullet.players.forEach { player ->
                builder.suggest(player.username)
            }

            return@SuggestionProvider CompletableFuture.completedFuture(builder.build())
        }
    }
}
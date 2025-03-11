package com.aznos.commands.commands.suggestions

import com.aznos.Bullet
import com.aznos.entity.player.Player
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

class GameModeSuggestions : SuggestionProvider<Player> {
    override fun getSuggestions(
        context: CommandContext<Player>?,
        builder: SuggestionsBuilder?)
    : CompletableFuture<Suggestions>? {
        Bullet.logger.info("GameModeSuggestions.getSuggestions")

        return builder
            ?.suggest("survival")
            ?.suggest("creative")
            ?.suggest("adventure")
            ?.suggest("spectator")
            ?.buildFuture()
    }
}
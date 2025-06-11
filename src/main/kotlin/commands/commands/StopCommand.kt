import com.aznos.Bullet
import com.aznos.commands.CommandCodes
import com.aznos.commands.CommandManager
import com.aznos.commands.CommandSource
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StopCommand {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSource>("stop")
                .then(
                    LiteralArgumentBuilder.literal<CommandSource>("broadcast")
                        .executes { context ->
                            val sender = context.source
                            if(!CommandManager.hasAdminPermission(sender)) {
                                return@executes CommandCodes.INVALID_PERMISSIONS.id
                            }

                            scheduleShutdown(60.seconds)
                            sender.sendMessage(Component.text(
                                "Server will shutdown in 60 seconds",
                                NamedTextColor.RED
                            ))

                            CommandCodes.SUCCESS.id
                        }
                        .then(
                            RequiredArgumentBuilder.argument<CommandSource, Int>(
                                "delay",
                                IntegerArgumentType.integer(1)
                            )
                                .executes { context ->
                                    val sender = context.source
                                    if(!CommandManager.hasAdminPermission(sender)) {
                                        return@executes CommandCodes.INVALID_PERMISSIONS.id
                                    }

                                    val delay = IntegerArgumentType.getInteger(context, "delay")
                                    scheduleShutdown(delay.seconds)
                                    sender.sendMessage(Component.text(
                                        "Server will shutdown in $delay seconds",
                                        NamedTextColor.RED
                                    ))

                                    CommandCodes.SUCCESS.id
                                }
                        )
                )
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun scheduleShutdown(delay: Duration) {
        GlobalScope.launch {
            val intervals = listOf(60, 30, 10, 5)
            for(interval in intervals) {
                if(delay.inWholeSeconds > interval) {
                    delay((delay.inWholeSeconds - interval).seconds)
                    Bullet.broadcast(Component.text("Server will shutdown in $interval seconds", NamedTextColor.RED))
                }
            }

            delay((delay.inWholeSeconds % 60).seconds)
            Bullet.close()
        }
    }
}
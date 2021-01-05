package com.abyssaldev.commands

import com.abyssaldev.commands.gateway.results.CommandExceptionResult
import com.abyssaldev.commands.gateway.results.CommandNotFoundResult
import com.abyssaldev.commands.gateway.results.NotEnoughParametersResult
import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.util.Loggable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandEngineAdapter internal constructor(private val commandEngine: CommandEngine): ListenerAdapter(), Loggable {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        GlobalScope.launch(Dispatchers.Default) {
            handleMessageReceived(event)
        }
    }

    private suspend fun handleMessageReceived(event: MessageReceivedEvent) {
        val result = commandEngine.handleMessage(event) ?: return

        when (result) {
            is NotEnoughParametersResult -> {
                event.channel.sendMessage(StringBuilder()
                    .appendLine(":x: I needed ${result.expectedParameterCount} parameters after `${result.command.name}`, but I only got ${result.suppliedParameterCount}.")
                    .appendLine()
                    .appendLine("For reference, here's how to use `${result.command.name}`:")
                    .appendLine("`" + commandEngine.prefixStrategy.getPrefix(event.guild).toString() + result.command.name + " " + result.command.parameters.joinToString(
                        " "
                    ) { c ->
                        "[" + c.name + "]"
                    } + "`")
                    .toString()
                ).queue()
            }
            is ParameterTypeParserResult<*> -> {
                event.channel.sendMessage(":x: ${result.reason}").queue()
            }
            is CommandExceptionResult -> {
                logger.error("Exception during ${result.command.name}!", result.throwable)
                event.channel.sendMessage(":x: ${result.reason}").queue()
            }
            is CommandNotFoundResult -> {
                if (commandEngine.showCommandNotFoundError) {
                    event.channel.sendMessage(":x: ${result.reason}").queue()
                }
            }
            else -> {
                when {
                    !result.isSuccess -> {
                        event.channel.sendMessage(":x: ${result.reason}").queue()
                    }
                }
            }
        }
    }
}
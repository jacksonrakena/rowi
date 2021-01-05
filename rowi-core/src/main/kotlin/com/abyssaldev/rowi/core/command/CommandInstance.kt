package com.abyssaldev.rowi.core.command

import com.abyssaldev.rowi.core.*
import kotlin.reflect.KFunction

class CommandInstance(
    override val name: String,
    override val description: String,
    val invoke: KFunction<*>,
    val parentModule: CommandModule,
    val parameters: List<CommandParameter>) : CommandBase, CommandExecutable<CommandRequest> {

    internal fun isMatch(token: String): Boolean {
        return this.name == token
    }

    override suspend fun invoke(call: CommandRequest, args: List<Any>): CommandResponse {
        return invoke.call(parentModule, call, *args.toTypedArray()) as CommandResponse
    }
}


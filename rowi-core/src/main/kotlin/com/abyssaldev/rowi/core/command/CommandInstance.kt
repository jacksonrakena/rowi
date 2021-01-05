package com.abyssaldev.rowi.core.command

import com.abyssaldev.rowi.core.*
import kotlin.reflect.KFunction

open class CommandInstance(
    override val name: String,
    override val description: String,
    val invoke: KFunction<*>,
    val parentModule: CommandModule,
    val parameters: List<CommandParameter>) : CommandBase, CommandExecutable<CommandRequest> {

    fun isMatch(token: String): Boolean {
        return this.name == token
    }

    override suspend fun invoke(call: CommandRequest, args: List<Any>): CommandResponse? {
        val invocationCallReturn = invoke.call(parentModule, call, *args.toTypedArray())
        return if (invocationCallReturn is CommandResponse) {
            invocationCallReturn
        } else { null }
    }
}


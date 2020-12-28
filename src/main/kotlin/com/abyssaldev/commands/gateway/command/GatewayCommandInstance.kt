package com.abyssaldev.commands.gateway.command

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.common.CommandBase
import com.abyssaldev.commands.common.CommandExecutable
import com.abyssaldev.commands.common.CommandModule
import net.dv8tion.jda.api.MessageBuilder
import kotlin.reflect.KFunction

class GatewayCommandInstance(
    override val name: String,
    override val description: String,
    val invoke: KFunction<*>,
    val parentModule: CommandModule,
    val parameters: List<GatewayCommandParameter>) : CommandBase, CommandExecutable<GatewayCommandRequest> {

    internal fun isMatch(token: String): Boolean {
        return this.name == token
    }

    override suspend fun invoke(call: GatewayCommandRequest, args: List<Any>): MessageBuilder? {
        return invoke.call(parentModule, call, *args.toTypedArray()) as? MessageBuilder
    }
}


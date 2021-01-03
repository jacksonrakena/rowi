package com.abyssaldev.commands.common

import net.dv8tion.jda.api.MessageBuilder

interface CommandExecutable<T: CommandRequest> : CommandBase {
    suspend fun invoke(call: T, args: List<Any>): MessageBuilder?
}
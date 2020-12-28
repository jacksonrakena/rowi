package com.abyssaldev.commands.framework.common

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*

abstract class CommandRequest {
    abstract val guild: Guild?
    abstract val channel: TextChannel
    abstract val member: Member?
    abstract val user: User
    abstract val jda: JDA
    abstract val rawArgs: List<String>
    open val args: ArgumentSet by lazy { ArgumentSet(rawArgs, this) }
    val botUser: SelfUser by lazy {
        jda.selfUser
    }
    open val botMember: Member? by lazy {
        guild?.selfMember
    }
}


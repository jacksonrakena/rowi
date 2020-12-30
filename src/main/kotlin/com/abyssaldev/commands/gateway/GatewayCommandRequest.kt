package com.abyssaldev.commands.gateway

import com.abyssaldev.commands.common.CommandRequest
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*

class GatewayCommandRequest(
    override val guild: Guild?,
    override val channel: TextChannel,
    override val member: Member?,
    override val user: User,
    override val jda: JDA,
    val message: Message,
    val flags: MutableList<String> = mutableListOf()
): CommandRequest() {
    override val rawArgs = listOf<String>()

    val isDebug
        get() = flags.contains("debug")

    val environment: Environment
        get() {
            return if (guild != null) { Environment.Guild } else { Environment.Dm }
        }

    enum class Environment {
        Guild,
        Dm
    }
}
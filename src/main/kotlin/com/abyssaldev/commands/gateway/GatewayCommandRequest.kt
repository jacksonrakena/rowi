package com.abyssaldev.commands.gateway

import com.abyssaldev.commands.common.CommandRequest
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*

open class GatewayCommandRequest(
    val message: Message,
    val flags: MutableList<String> = mutableListOf()
): CommandRequest() {
    override val rawArgs = listOf<String>()

    override val guild: Guild? by lazy {
        if (channel is TextChannel) {
            (channel as TextChannel).guild
        } else {
            null
        }
    }

    override val user: User by lazy {
        message.author
    }

    override val channel: MessageChannel by lazy {
        message.channel
    }

    override val member: Member? by lazy {
        message.member
    }

    override val jda: JDA
        get() = channel.jda

    val isDebug: Boolean by lazy {
        flags.contains("debug")
    }

    val environment: Environment
        get() {
            return if (guild != null) { Environment.Guild } else { Environment.Dm }
        }

    enum class Environment {
        Guild,
        Dm
    }
}
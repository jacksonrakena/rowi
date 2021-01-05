package com.abyssaldev.rowi.jda

import com.abyssaldev.rowi.core.CommandRequest
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*

open class JdaCommandRequest(
    val message: Message,
    override val flags: MutableList<String> = mutableListOf(),
    override val rawArgs: List<String> = listOf(),
    override var rawString: String = ""
): CommandRequest() {

    val guild: Guild? by lazy {
        if (channel is TextChannel) {
            (channel as TextChannel).guild
        } else {
            null
        }
    }

    val user: User by lazy {
        message.author
    }

    val channel: MessageChannel by lazy {
        message.channel
    }

    val member: Member? by lazy {
        message.member
    }

    val jda: JDA
        get() = channel.jda

    val botUser: SelfUser by lazy {
        jda.selfUser
    }
    open val botMember: Member? by lazy {
        guild?.selfMember
    }

    val environment: Environment
        get() {
            return if (guild != null) {
                Environment.Guild
            } else {
                Environment.Dm
            }
        }

    enum class Environment {
        Guild,
        Dm
    }
}
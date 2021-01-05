package com.abyssaldev.rowi.catnip

import com.abyssaldev.rowi.core.CommandRequest
import com.mewna.catnip.Catnip
import com.mewna.catnip.entity.channel.MessageChannel
import com.mewna.catnip.entity.guild.Guild
import com.mewna.catnip.entity.guild.Member
import com.mewna.catnip.entity.message.Message
import com.mewna.catnip.entity.user.User

class CatnipCommandRequest(val message: Message): CommandRequest() {
    override var rawString: String = ""
    override val rawArgs: List<String> = listOf()
    override val flags: MutableList<String> = mutableListOf()

    val guild: Guild? by lazy {
        message.guild().blockingGet()
    }

    val user: User by lazy {
        message.author()
    }

    val channel: MessageChannel by lazy {
        message.channel().blockingGet()
    }

    val member: Member? by lazy {
        message.member()
    }

    val catnip: Catnip
        get() = message.catnip()

    val botUser: User by lazy {
        catnip.selfUser().blockingGet()
    }

    val botMember: Member? by lazy {
        guild?.selfMember()?.blockingGet()
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
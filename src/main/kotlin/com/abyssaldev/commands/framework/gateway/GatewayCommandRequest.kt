package com.abyssaldev.commands.framework.gateway

import com.abyssaldev.commands.framework.common.CommandRequest
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*

class GatewayCommandRequest(
    override val guild: Guild?,
    override val channel: TextChannel,
    override val member: Member?,
    override val user: User,
    override val jda: JDA,
    val message: Message
): CommandRequest() {
    override val rawArgs = listOf<String>()
}
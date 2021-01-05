package com.abyssaldev.rowi.jda

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.CommandResponse
import net.dv8tion.jda.api.MessageBuilder

class JdaCommandResponse (isSuccess: Boolean, val messageBuilder: MessageBuilder): CommandResponse(isSuccess, "") {
    override fun completeResponse(request: CommandRequest) {
        if (request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("JdaCommandResponse")
        if (!messageBuilder.isEmpty) {
            request.channel.sendMessage(messageBuilder.build()).queue()
        }
    }
}
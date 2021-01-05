package com.abyssaldev.rowi.jda

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder

interface Responder {
    fun respond(responder: MessageBuilder.() -> Unit): JdaCommandResponse {
        val builder = MessageBuilder()
        responder(builder)
        return JdaCommandResponse(true, builder)
    }

    fun respondEmbed(responder: EmbedBuilder.() -> Unit): JdaCommandResponse
        = JdaCommandResponse(true, MessageBuilder().embed(responder))

    fun respond(content: String): JdaCommandResponse
        = JdaCommandResponse(true, MessageBuilder().setContent(content))

    fun MessageBuilder.content(content: String) = apply {
        setContent(content)
    }

    fun MessageBuilder.embed(builder: EmbedBuilder.() -> Unit) = apply {
        setEmbed(EmbedBuilder().apply(builder).build())
    }

    fun EmbedBuilder.appendDescriptionLine(line: String) = apply {
        this.descriptionBuilder.appendLine(line)
    }
}
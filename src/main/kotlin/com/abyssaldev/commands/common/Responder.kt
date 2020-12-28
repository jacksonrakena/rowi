package com.abyssaldev.commands.common

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder

interface Responder {
    fun respond(responder: MessageBuilder.() -> Unit): MessageBuilder {
        val builder = MessageBuilder()
        responder(builder)
        return builder
    }

    fun respondEmbed(responder: EmbedBuilder.() -> Unit): MessageBuilder = MessageBuilder().embed(responder)

    fun respond(content: String): MessageBuilder {
        return MessageBuilder().setContent(content)
    }

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
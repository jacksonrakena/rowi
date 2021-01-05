package com.abyssaldev.rowi.jda

import com.abyssaldev.rowi.core.util.tryAndIgnoreExceptions
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import java.lang.UnsupportedOperationException

fun MessageChannel.trySendMessage(m: Message) = tryAndIgnoreExceptions { this.sendMessage(m).queue() }
fun MessageChannel.trySendMessage(m: CharSequence) = tryAndIgnoreExceptions { this.sendMessage(m).queue() }

internal class JdaCompatibleErrors {
    companion object {
        fun usedOnInvalidObject(type: String): UnsupportedOperationException {
            return UnsupportedOperationException("A $type was used in a non-JdaCommandRequest compatible environment.")
        }
    }
}
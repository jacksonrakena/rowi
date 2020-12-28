package com.abyssaldev.abyssal_command_engine.util

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel

inline fun <reified T> List<Annotation>.getAnnotation(): T? {
    return this.filterIsInstance<T>().firstOrNull()
}

inline fun <reified T> List<Annotation>.getAnnotations(): List<T> {
    return this.filterIsInstance<T>().toList()
}

inline fun <T> tryAndIgnoreExceptions(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {

    }

fun MessageChannel.trySendMessage(m: Message) = tryAndIgnoreExceptions { this.sendMessage(m).queue() }
fun MessageChannel.trySendMessage(m: CharSequence) = tryAndIgnoreExceptions { this.sendMessage(m).queue() }

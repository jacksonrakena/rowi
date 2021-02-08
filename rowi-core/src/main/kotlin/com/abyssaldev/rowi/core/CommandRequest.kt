package com.abyssaldev.rowi.core

abstract class CommandRequest {
    abstract var rawString: String
    abstract val rawArgs: List<String>
    open val argumentSet: ArgumentSet by lazy { ArgumentSet(rawArgs, this) }
    abstract val flags: MutableList<String>
    abstract val engine: CommandEngine
    val isDebug: Boolean by lazy {
        flags.contains("debug")
    }
}


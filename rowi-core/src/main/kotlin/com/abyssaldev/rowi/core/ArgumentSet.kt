package com.abyssaldev.rowi.core

open class ArgumentSet(internal val argSet: List<String>, internal open val request: CommandRequest) {
    fun ordinal(position: Int): ArgumentValue? {
        val str = argSet.getOrNull(position)
        return if (str != null) {
            ArgumentValue(str, request)
        } else {
            null
        }
    }

    fun asList(): List<String> = argSet

    class Named(val argSetNamed: HashMap<String, String>, override val request: CommandRequest) : ArgumentSet(
        argSetNamed.values.toList(),
        request
    ) {
        fun named(name: String): ArgumentValue.Named {
            return ArgumentValue.Named(argSetNamed[name]!!, request)
        }
    }

    open class ArgumentValue(val value: String, val request: CommandRequest) {
        open val integer: Int? by lazy {
            value.toIntOrNull()
        }

        open val long: Long? by lazy {
            value.toLongOrNull()
        }

        open val boolean: Boolean? by lazy {
            if (value != "true" && value != "false") {
                null
            } else {
                value.toBoolean()
            }
        }

        val string: String = value

        class Named(value: String, request: CommandRequest) : ArgumentValue(value, request)
    }
}
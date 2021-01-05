package com.abyssaldev.rowi.core.command

import kotlin.reflect.KClass

class CommandParameter(
    val name: String,
    val description: String,
    val contractIds: List<String>,
    val type: KClass<*>
)
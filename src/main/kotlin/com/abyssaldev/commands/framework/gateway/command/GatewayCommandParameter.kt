package com.abyssaldev.commands.framework.gateway.command

import com.abyssaldev.commands.framework.gateway.reflect.ArgumentContract
import kotlin.reflect.KClass

class GatewayCommandParameter(
    val name: String,
    val description: String,
    val contracts: List<ArgumentContract<*>>,
    val type: KClass<*>
)
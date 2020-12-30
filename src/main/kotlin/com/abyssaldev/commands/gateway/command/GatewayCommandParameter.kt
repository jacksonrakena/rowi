package com.abyssaldev.commands.gateway.command

import com.abyssaldev.commands.gateway.contracts.ArgumentContract
import kotlin.reflect.KClass

class GatewayCommandParameter(
    val name: String,
    val description: String,
    val contracts: List<String>,
    val type: KClass<*>
)
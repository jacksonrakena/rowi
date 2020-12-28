package com.abyssaldev.abyssal_command_engine.framework.gateway.command

import com.abyssaldev.abyssal_command_engine.framework.gateway.reflect.ArgumentContract
import kotlin.reflect.KType

class GatewayCommandParameter(
    val name: String,
    val description: String,
    val contracts: List<ArgumentContract>,
    val type: KType
)
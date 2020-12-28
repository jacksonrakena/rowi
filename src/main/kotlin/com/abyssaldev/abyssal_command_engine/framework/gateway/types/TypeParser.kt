package com.abyssaldev.abyssal_command_engine.framework.gateway.types

import com.abyssaldev.abyssal_command_engine.framework.gateway.results.ParameterTypeParserResult
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandParameter

interface TypeParser<T> {
    fun parse(value: String, parameter: GatewayCommandParameter) : ParameterTypeParserResult<T>
}
package com.abyssaldev.commands.framework.gateway.types

import com.abyssaldev.commands.framework.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.framework.gateway.command.GatewayCommandParameter

interface TypeParser<T> {
    fun parse(value: String, parameter: GatewayCommandParameter) : ParameterTypeParserResult<T>
}
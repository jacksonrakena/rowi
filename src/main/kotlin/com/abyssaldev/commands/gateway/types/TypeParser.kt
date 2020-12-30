package com.abyssaldev.commands.gateway.types

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter

interface TypeParser<T> {
    fun parse(value: String, request: GatewayCommandRequest, parameter: GatewayCommandParameter) : ParameterTypeParserResult<T>
}
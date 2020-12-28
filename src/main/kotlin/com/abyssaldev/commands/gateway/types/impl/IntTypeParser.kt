package com.abyssaldev.commands.gateway.types.impl

import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.gateway.types.TypeParser

class IntTypeParser : TypeParser<Int> {
    override fun parse(value: String, parameter: GatewayCommandParameter): ParameterTypeParserResult<Int> {
        val parsed = value.toIntOrNull()
            ?: return ParameterTypeParserResult.failure("The provided value was not an integer.", parameter)
        return ParameterTypeParserResult.success(parsed, parameter)
    }
}
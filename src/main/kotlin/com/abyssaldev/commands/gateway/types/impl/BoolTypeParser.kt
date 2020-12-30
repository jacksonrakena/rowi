package com.abyssaldev.commands.gateway.types.impl

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.gateway.types.TypeParser

class BoolTypeParser: TypeParser<Boolean> {
    private val trueValues = listOf("true", "t", "yes", "y", "1", "ye", "ya")
    private val falseValues = listOf("false", "f", "no", "n", "0", "na")
    override fun parse(value: String, request: GatewayCommandRequest, parameter: GatewayCommandParameter): ParameterTypeParserResult<Boolean> = when {
        trueValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(true, parameter)
        falseValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(false, parameter)
        else -> ParameterTypeParserResult.failure("The provided value is not a valid boolean.", parameter)
    }
}
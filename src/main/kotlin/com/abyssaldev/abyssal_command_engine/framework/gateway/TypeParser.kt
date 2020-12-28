package com.abyssaldev.abyssal_command_engine.framework.gateway

import com.abyssaldev.abyssal_command_engine.framework.common.result.ParameterTypeParserResult
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandParameter

interface TypeParser<T> {
    fun parse(value: String, parameter: GatewayCommandParameter) : ParameterTypeParserResult<T>
}

class IntTypeParser : TypeParser<Int> {
    override fun parse(value: String, parameter: GatewayCommandParameter): ParameterTypeParserResult<Int> {
        val parsed = value.toIntOrNull()
            ?: return ParameterTypeParserResult.failure("The provided value was not an integer.", parameter)
        return ParameterTypeParserResult.success(parsed, parameter)
    }
}

class BoolTypeParser: TypeParser<Boolean> {
    val trueValues = listOf("true", "t", "yes", "y", "1", "ye", "ya")
    val falseValues = listOf("false", "f", "no", "n", "0", "na")
    override fun parse(value: String, parameter: GatewayCommandParameter): ParameterTypeParserResult<Boolean> = when {
        trueValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(true, parameter)
        trueValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(false, parameter)
        else -> ParameterTypeParserResult.failure("The provided value is not a valid boolean.", parameter)
    }
}
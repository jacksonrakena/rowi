package com.abyssaldev.rowi.core.types.impl

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.results.ParameterTypeParserResult
import com.abyssaldev.rowi.core.types.TypeParser

class BoolTypeParser: TypeParser<Boolean> {
    private val trueValues = listOf("true", "t", "yes", "y", "1", "ye", "ya")
    private val falseValues = listOf("false", "f", "no", "n", "0", "na")
    override fun parse(value: String, request: CommandRequest, parameter: CommandParameter): ParameterTypeParserResult<Boolean> = when {
        trueValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(true, parameter)
        falseValues.any { it.equals(value, ignoreCase = true) } -> ParameterTypeParserResult.success(false, parameter)
        else -> ParameterTypeParserResult.failure("The provided value is not a valid boolean.", parameter)
    }
}
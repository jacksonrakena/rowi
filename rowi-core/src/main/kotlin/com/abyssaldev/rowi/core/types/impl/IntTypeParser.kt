package com.abyssaldev.rowi.core.types.impl

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.results.ParameterTypeParserResult
import com.abyssaldev.rowi.core.types.TypeParser

class IntTypeParser : TypeParser<Int> {
    override fun parse(value: String, request: CommandRequest, parameter: CommandParameter): ParameterTypeParserResult<Int> {
        val parsed = value.toIntOrNull()
            ?: return ParameterTypeParserResult.failure("The provided value was not an integer.", parameter)
        return ParameterTypeParserResult.success(parsed, parameter)
    }
}
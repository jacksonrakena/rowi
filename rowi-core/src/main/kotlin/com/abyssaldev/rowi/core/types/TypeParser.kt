package com.abyssaldev.rowi.core.types

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.results.ParameterTypeParserResult

interface TypeParser<T> {
    fun parse(value: String, request: CommandRequest, parameter: CommandParameter) : ParameterTypeParserResult<T>
}
package com.abyssaldev.rowi.core.types.impl

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.results.ParameterTypeParserResult
import com.abyssaldev.rowi.core.types.TypeParser

class LongTypeParser: TypeParser<Long> {
    override fun parse(value: String, request: CommandRequest, parameter: CommandParameter): ParameterTypeParserResult<Long> {
        val long = value.toLongOrNull()
        return if (long == null) {
            ParameterTypeParserResult.failure("The provided value was not a long.", parameter)
        } else {
            ParameterTypeParserResult.success(long, parameter)
        }
    }
}
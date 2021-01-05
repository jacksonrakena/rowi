package com.abyssaldev.rowi.core.results

import com.abyssaldev.rowi.core.command.CommandParameter

class ParameterTypeParserResult<T>(val result: T?, isSuccess: Boolean, reason: String?, val parameter: CommandParameter) : Result(isSuccess, reason) {
    companion object {
        fun <T> success(result: T, parameter: CommandParameter): ParameterTypeParserResult<T> {
            return ParameterTypeParserResult(result, true, null, parameter)
        }

        fun <T> failure(reason: String, parameter: CommandParameter): ParameterTypeParserResult<T> {
            return ParameterTypeParserResult(null, false, reason, parameter)
        }
    }
}
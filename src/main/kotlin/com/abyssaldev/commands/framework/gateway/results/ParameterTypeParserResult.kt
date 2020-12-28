package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.framework.common.Result

class ParameterTypeParserResult<T>(val result: T?, isSuccess: Boolean, reason: String?, val parameter: GatewayCommandParameter) : Result(isSuccess, reason) {
    companion object {
        fun <T> success(result: T, parameter: GatewayCommandParameter): ParameterTypeParserResult<T> {
            return ParameterTypeParserResult(result, true, null, parameter)
        }

        fun <T> failure(reason: String, parameter: GatewayCommandParameter): ParameterTypeParserResult<T> {
            return ParameterTypeParserResult(null, false, reason, parameter)
        }
    }
}
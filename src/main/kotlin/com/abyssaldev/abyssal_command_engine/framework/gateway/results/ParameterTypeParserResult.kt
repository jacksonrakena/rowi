package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandParameter
import com.abyssaldev.abyssal_command_engine.framework.common.Result

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
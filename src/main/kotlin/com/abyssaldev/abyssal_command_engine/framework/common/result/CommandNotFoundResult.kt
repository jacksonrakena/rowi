package com.abyssaldev.abyssal_command_engine.framework.common.result

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandInternal
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandParameter
import com.abyssaldev.abyssal_command_engine.framework.gateway.reflect.Result
import kotlin.reflect.KClass

data class CommandNotFoundResult(val commandToken: String): Result(false, "A command by the name of `${commandToken}` was not found.")

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")

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
data class ParameterTypeParserMissingResult(val type: KClass<*>): Result(false, "No type parser is registered for type `${type.simpleName!!}`.")

data class ParameterTypeParserExceptionResult(val throwable: Throwable, var type: KClass<*>): Result(false, "The type parser for `${type.simpleName}` threw an error.")

data class CommandExceptionResult(val throwable: Throwable, var command: GatewayCommandInternal) : Result(false, "There was an internal error executing that command.")
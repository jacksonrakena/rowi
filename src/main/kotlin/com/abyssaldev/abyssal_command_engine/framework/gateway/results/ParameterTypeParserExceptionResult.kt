package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.common.Result
import kotlin.reflect.KClass

data class ParameterTypeParserExceptionResult(val throwable: Throwable, var type: KClass<*>): Result(false, "The type parser for `${type.simpleName}` threw an error.")
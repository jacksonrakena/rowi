package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result
import kotlin.reflect.KClass

data class ParameterTypeParserExceptionResult(val throwable: Throwable, var type: KClass<*>): Result(false, "The type parser for `${type.simpleName}` threw an error.")
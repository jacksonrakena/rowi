package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.common.Result
import kotlin.reflect.KClass

data class ParameterTypeParserExceptionResult(val throwable: Throwable, var type: KClass<*>): Result(false, "The type parser for `${type.simpleName}` threw an error.")
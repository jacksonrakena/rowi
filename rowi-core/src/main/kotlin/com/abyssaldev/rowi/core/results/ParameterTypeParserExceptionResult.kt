package com.abyssaldev.rowi.core.results

import kotlin.reflect.KClass

data class ParameterTypeParserExceptionResult(val throwable: Throwable, var type: KClass<*>): Result(false, "The type parser for `${type.simpleName}` threw an error.")
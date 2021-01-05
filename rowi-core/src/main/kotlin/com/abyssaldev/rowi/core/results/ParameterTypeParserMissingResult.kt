package com.abyssaldev.rowi.core.results

import kotlin.reflect.KClass

data class ParameterTypeParserMissingResult(val type: KClass<*>): Result(false, "No type parser is registered for type `${type.simpleName!!}`.")
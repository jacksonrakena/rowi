package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result
import kotlin.reflect.KClass

data class ParameterTypeParserMissingResult(val type: KClass<*>): Result(false, "No type parser is registered for type `${type.simpleName!!}`.")
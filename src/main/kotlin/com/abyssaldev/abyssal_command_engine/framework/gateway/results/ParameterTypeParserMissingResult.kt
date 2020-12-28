package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.common.Result
import kotlin.reflect.KClass

data class ParameterTypeParserMissingResult(val type: KClass<*>): Result(false, "No type parser is registered for type `${type.simpleName!!}`.")
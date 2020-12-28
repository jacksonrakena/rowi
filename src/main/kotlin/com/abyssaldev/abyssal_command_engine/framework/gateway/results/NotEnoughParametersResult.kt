package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.common.Result

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")
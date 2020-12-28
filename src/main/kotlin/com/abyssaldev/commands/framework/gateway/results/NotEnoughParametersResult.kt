package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.common.Result

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")
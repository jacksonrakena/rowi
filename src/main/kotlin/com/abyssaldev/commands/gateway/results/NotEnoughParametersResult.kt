package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")
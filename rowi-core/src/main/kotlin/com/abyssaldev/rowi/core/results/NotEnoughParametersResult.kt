package com.abyssaldev.rowi.core.results

import com.abyssaldev.rowi.core.command.CommandInstance

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int, val command: CommandInstance) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")
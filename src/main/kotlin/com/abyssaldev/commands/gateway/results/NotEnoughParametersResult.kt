package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result
import com.abyssaldev.commands.gateway.command.GatewayCommandInstance

data class NotEnoughParametersResult(val suppliedParameterCount: Int, val expectedParameterCount: Int, val command: GatewayCommandInstance) : Result(false, "${expectedParameterCount} parameters were expected, but only ${suppliedParameterCount} were given.")
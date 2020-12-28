package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.gateway.command.GatewayCommandInstance
import com.abyssaldev.commands.common.Result

data class CommandExceptionResult(val throwable: Throwable, var command: GatewayCommandInstance) : Result(false, "There was an internal error executing that command.")
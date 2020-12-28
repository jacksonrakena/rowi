package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.gateway.command.GatewayCommandInstance
import com.abyssaldev.commands.framework.common.Result

data class CommandExceptionResult(val throwable: Throwable, var command: GatewayCommandInstance) : Result(false, "There was an internal error executing that command.")
package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandInstance
import com.abyssaldev.abyssal_command_engine.framework.common.Result

data class CommandExceptionResult(val throwable: Throwable, var command: GatewayCommandInstance) : Result(false, "There was an internal error executing that command.")
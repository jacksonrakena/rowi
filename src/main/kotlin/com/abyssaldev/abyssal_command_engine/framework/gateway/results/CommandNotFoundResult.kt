package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.common.Result

data class CommandNotFoundResult(val commandToken: String): Result(false, "A command by the name of `${commandToken}` was not found.")
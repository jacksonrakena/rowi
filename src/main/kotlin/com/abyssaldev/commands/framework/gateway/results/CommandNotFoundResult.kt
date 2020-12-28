package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.common.Result

data class CommandNotFoundResult(val commandToken: String): Result(false, "A command by the name of `${commandToken}` was not found.")
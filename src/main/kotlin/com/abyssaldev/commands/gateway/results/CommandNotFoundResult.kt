package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result

data class CommandNotFoundResult(val commandToken: String): Result(false, "A command by the name of `${commandToken}` was not found.")
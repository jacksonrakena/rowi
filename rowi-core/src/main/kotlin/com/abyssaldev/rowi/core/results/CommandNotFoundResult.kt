package com.abyssaldev.rowi.core.results

data class CommandNotFoundResult(val commandToken: String): Result(false, "A command by the name of `${commandToken}` was not found.")
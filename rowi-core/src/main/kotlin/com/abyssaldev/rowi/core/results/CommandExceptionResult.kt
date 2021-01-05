package com.abyssaldev.rowi.core.results

import com.abyssaldev.rowi.core.command.CommandInstance

data class CommandExceptionResult(val throwable: Throwable, var command: CommandInstance) : Result(false, "There was an internal error executing that command.")
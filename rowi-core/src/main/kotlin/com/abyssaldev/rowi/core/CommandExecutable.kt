package com.abyssaldev.rowi.core

interface CommandExecutable<T: CommandRequest> : CommandBase {
    suspend fun invoke(call: T, args: List<Any>): CommandResponse?
}
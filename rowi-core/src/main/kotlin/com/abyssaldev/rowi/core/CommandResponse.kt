package com.abyssaldev.rowi.core

import com.abyssaldev.rowi.core.results.Result

abstract class CommandResponse(isSuccess: Boolean, reason: String?) : Result(isSuccess, reason) {
    abstract fun completeResponse(request: CommandRequest)
}
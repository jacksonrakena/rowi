package com.abyssaldev.rowi.core

import com.abyssaldev.rowi.core.results.Result

open class CommandResponse(isSuccess: Boolean, reason: String?) : Result(isSuccess, reason)
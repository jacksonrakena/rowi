package com.abyssaldev.rowi.core.results

import com.abyssaldev.rowi.core.CommandResponse

class SuccessfulResult(val response: CommandResponse): Result(true, "")
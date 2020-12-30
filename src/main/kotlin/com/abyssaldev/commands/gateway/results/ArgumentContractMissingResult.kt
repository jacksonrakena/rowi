package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result
import kotlin.reflect.KClass

data class ArgumentContractMissingResult(val contractId: String): Result(false, "No argument contract is configured for contract ID `${contractId}`.")
package com.abyssaldev.rowi.core.results

data class ArgumentContractMissingResult(val contractId: String): Result(false, "No argument contract is configured for contract ID `${contractId}`.")
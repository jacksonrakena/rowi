package com.abyssaldev.rowi.core.contracts

import com.abyssaldev.rowi.core.CommandRequest

interface ArgumentContractable<T> {
    fun evaluateContract(argument: SuppliedArgument<T>, request: CommandRequest) : ArgumentContract.Result<T>
}
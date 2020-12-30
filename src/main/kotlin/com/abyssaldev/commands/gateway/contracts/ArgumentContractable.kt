package com.abyssaldev.commands.gateway.contracts

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.common.SuppliedArgument

interface ArgumentContractable<T> {
    fun evaluateContract(argument: SuppliedArgument<T>, request: GatewayCommandRequest) : ArgumentContract.Result<T>
}
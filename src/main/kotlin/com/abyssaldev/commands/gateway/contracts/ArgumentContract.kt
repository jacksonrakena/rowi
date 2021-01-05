package com.abyssaldev.commands.gateway.contracts

import com.abyssaldev.commands.gateway.common.SuppliedArgument
import com.abyssaldev.commands.gateway.contracts.impl.NotBotContract

@Repeatable
annotation class ArgumentContract(val contractId: String) {
    class Result<T>(
        isSuccess: Boolean,
        reason: String?,
        val argument: SuppliedArgument<T>,
        val contract: ArgumentContractable<T>) : com.abyssaldev.commands.common.Result(isSuccess, reason) {
        companion object {
            fun <T> success(argument: SuppliedArgument<T>, contract: ArgumentContractable<T>): Result<T> {
                return Result(true, null, argument, contract)
            }

            fun <T> failure(reason: String, argument: SuppliedArgument<T>, contract: ArgumentContractable<T>): Result<T> {
                return Result(false, reason, argument, contract)
            }
        }
    }
}
package com.abyssaldev.abyssal_command_engine.framework.gateway.reflect

data class SuppliedArgument<T>(val name: String, val value: T)

open class Result(val isSuccess: Boolean, val reason: String?)

interface ArgumentContractable<T> {
    fun evaluateContract(argument: SuppliedArgument<T>) : ArgumentContract.Result<T>
}

class StringLengthContract(val minLength: Int) : ArgumentContractable<String> {
    override fun evaluateContract(argument: SuppliedArgument<String>) : ArgumentContract.Result<String> {
        return if (argument.value.length >= minLength) {
            ArgumentContract.Result.success(argument, this)
        } else {
            ArgumentContract.Result.failure("`${argument.name}` (length ${argument.value.length}) was less than ${minLength} characters long.", argument, this)
        }
    }
}

@ArgumentContract<StringLengthContract>()
annotation class ArgumentContract<T: ArgumentContractable<*>> {
    class Result<T>(
        isSuccess: Boolean,
        reason: String?,
        val argument: SuppliedArgument<T>,
        val contract: ArgumentContractable<T>) : com.abyssaldev.abyssal_command_engine.framework.gateway.reflect.Result(isSuccess, reason) {
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
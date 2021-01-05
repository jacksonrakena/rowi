package com.abyssaldev.rowi.jda.impl

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.jda.JdaCommandRequest
import com.abyssaldev.rowi.core.contracts.SuppliedArgument
import com.abyssaldev.rowi.core.contracts.ArgumentContract
import com.abyssaldev.rowi.core.contracts.ArgumentContractable
import com.abyssaldev.rowi.jda.JdaCompatibleErrors
import net.dv8tion.jda.api.entities.Member

class NotCallerContract: ArgumentContractable<Member> {
    companion object {
        val id = "not-caller"
    }

    override fun evaluateContract(
        argument: SuppliedArgument<Member>,
        request: CommandRequest
    ): ArgumentContract.Result<Member> {
        if (request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("NotCallerContract")

        return if (argument.value.id == request.user.id) {
            ArgumentContract.Result.failure("`${argument.name}` cannot be you.", argument, this)
        } else {
            ArgumentContract.Result.success(argument, this)
        }
    }
}
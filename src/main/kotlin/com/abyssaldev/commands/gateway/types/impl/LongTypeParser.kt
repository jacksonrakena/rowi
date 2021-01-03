package com.abyssaldev.commands.gateway.types.impl

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.gateway.types.TypeParser

class LongTypeParser: TypeParser<Long> {
    override fun parse(value: String, request: GatewayCommandRequest, parameter: GatewayCommandParameter): ParameterTypeParserResult<Long> {
        val long = value.toLongOrNull()
        return if (long == null) {
            ParameterTypeParserResult.failure("The provided value was not a long.", parameter)
        } else {
            ParameterTypeParserResult.success(long, parameter)
        }
    }
}
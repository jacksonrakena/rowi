package com.abyssaldev.commands.gateway.types.impl

import com.abyssaldev.commands.gateway.GatewayCommandRequest
import com.abyssaldev.commands.gateway.command.GatewayCommandParameter
import com.abyssaldev.commands.gateway.results.ParameterTypeParserResult
import com.abyssaldev.commands.gateway.types.TypeParser
import net.dv8tion.jda.api.entities.Member

class MemberTypeParser : TypeParser<Member> {
    fun readLong(
        value: Long,
        request: GatewayCommandRequest,
        parameter: GatewayCommandParameter
    ): ParameterTypeParserResult<Member> {
        val member = request.guild!!.getMemberById(value)
        return if (member != null) {
            ParameterTypeParserResult.success(member, parameter)
        } else {
            ParameterTypeParserResult.failure("`${value}` isn't a member of this server.", parameter)
        }
    }

    override fun parse(
        value: String,
        request: GatewayCommandRequest,
        parameter: GatewayCommandParameter
    ): ParameterTypeParserResult<Member> {
        // Environment check
        if (request.environment != GatewayCommandRequest.Environment.Guild) {
            return ParameterTypeParserResult.failure("This command can only be used in a server.", parameter)
        }

        // Raw user ID parse
        var longParse = value.toLongOrNull()
        if (longParse != null) {
            return readLong(longParse, request, parameter)
        }

        // Mention (ex. <@255950165200994307>)
        if (value.startsWith("<@") && value.endsWith(">")) {
            longParse = if (value[2] == '!') {
                value.substring(3, value.length - 1).toLongOrNull()
            } else {
                value.substring(2, value.length - 1).toLongOrNull()
            }
            return if (longParse != null) readLong(longParse, request, parameter)
            else ParameterTypeParserResult.failure("`${value}` isn't a valid member of this server.", parameter)
        }

        if (value.indexOf('#') != -1) {
            val hashMember = request.guild!!.getMemberByTag(value)
            return if (hashMember != null) {
                ParameterTypeParserResult.success(hashMember, parameter)
            } else {
                ParameterTypeParserResult.failure("`${value}` isn't a valid member of this server.", parameter)
            }
        }

        val legacyMatchingMember = request.guild!!.members.filter {
            it.nickname.equals(value, true) || it.user.name.equals(value, true)
        }.firstOrNull()
        return if (legacyMatchingMember != null) {
            ParameterTypeParserResult.success(legacyMatchingMember, parameter)
        } else {
            ParameterTypeParserResult.failure("`${value}` isn't a valid member of this server.", parameter)
        }
    }
}
package com.abyssaldev.abyssal_command_engine.framework.gateway.event

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandInternal
import com.abyssaldev.abyssal_command_engine.framework.gateway.GatewayCommandRequest
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandFailure
import com.abyssaldev.abyssal_command_engine.util.trySendMessage

/**
 * The default event handler for a [CommandEngine].
 */
open class DefaultCommandEngineEventHandler : CommandEngineEventHandler {
    override fun onGatewayCommandRegistered(command: GatewayCommandInternal) {}

    override fun onGatewayCommandSuccess(command: GatewayCommandInternal, request: GatewayCommandRequest) {}

    override fun onGatewayCommandFailure(
        command: GatewayCommandInternal,
        request: GatewayCommandRequest,
        failure: GatewayCommandFailure,
        data: String
    ) {
        val message = when (failure) {
            GatewayCommandFailure.FailedOwnerRestrictionCheck -> "This command is only available for the bot owner."
            GatewayCommandFailure.MissingBotPermissions -> "I'm missing some permissions: ${data}"
            GatewayCommandFailure.MissingUserPermissions -> "You're missing some permissions: ${data}."
            GatewayCommandFailure.MissingUserRole -> "You need to be a member of the role '${data}'."
            GatewayCommandFailure.InternalError -> "There was an internal error processing your command."
            GatewayCommandFailure.FailedCanInvokeCheck -> data
        }

        request.channel.trySendMessage(message)
    }
}
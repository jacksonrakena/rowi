package com.abyssaldev.abyssal_command_engine.framework.gateway.event

import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandInternal
import com.abyssaldev.abyss.framework.gateway.GatewayCommandRequest
import com.abyssaldev.abyssal_command_engine.framework.gateway.command.GatewayCommandFailure

interface CommandEngineEventHandler {
    /**
     * Invoked when a [GatewayCommandInternal] is discovered by a [CommandEngine].
     */
    fun onGatewayCommandRegistered(command: GatewayCommandInternal)

    /**
     * Invoked when a [GatewayCommandInternal] succeeds.
     */
    fun onGatewayCommandSuccess(command: GatewayCommandInternal, request: GatewayCommandRequest)

    /**
     * Invoked when a [GatewayCommandInternal] fails.
     */
    fun onGatewayCommandFailure(command: GatewayCommandInternal, request: GatewayCommandRequest, failure: GatewayCommandFailure, data: String)
}
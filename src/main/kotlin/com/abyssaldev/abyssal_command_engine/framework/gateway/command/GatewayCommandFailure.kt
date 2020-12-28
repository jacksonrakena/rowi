package com.abyssaldev.abyssal_command_engine.framework.gateway.command

enum class GatewayCommandFailure {
    FailedOwnerRestrictionCheck,
    MissingBotPermissions,
    MissingUserPermissions,
    MissingUserRole,
    InternalError,
    FailedCanInvokeCheck
}
package com.abyssaldev.abyssal_command_engine.framework.gateway.reflect

import com.abyssaldev.abyssal_command_engine.framework.common.CommandRequest

enum class ContractType(val filter: (Any, CommandRequest) -> String?) {
    NO_ALLOW_INVOKER({ it, request ->
        if (it == request.user.id) null
        else "You can't choose yourself."
    }),
    NO_ALLOW_BOT({it, request ->
        if (it == request.botUser.id) null
        else "You can't choose me."
    })
}
package com.abyssaldev.abyssal_command_engine.framework.gateway.results

import com.abyssaldev.abyssal_command_engine.framework.common.Result

class NotACommandRequestResult : Result(false, "The message did not contain the prefix, or was sent by a bot.")
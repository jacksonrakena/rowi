package com.abyssaldev.commands.framework.gateway.results

import com.abyssaldev.commands.framework.common.Result

class NotACommandRequestResult : Result(false, "The message did not contain the prefix, or was sent by a bot.")
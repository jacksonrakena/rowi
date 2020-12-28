package com.abyssaldev.commands.gateway.results

import com.abyssaldev.commands.common.Result

class NotACommandRequestResult : Result(false, "The message did not contain the prefix, or was sent by a bot.")
package com.abyssaldev.rowi.core.parsing

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter

interface ContentParser {
    fun parse(input: CommandRequest): List<String>
}
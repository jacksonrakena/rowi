package com.abyssaldev.rowi.core.parsing.impl

import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.parsing.ContentParser

class DefaultContentParser : ContentParser {
    override fun parse(input: CommandRequest): List<String> {
        val string = input.rawString
        val quoteCharacter = '"'
        val whitespaceCharacter = ' '
        var isInQuote = false
        val buffer = StringBuffer()
        val stringComponents = mutableListOf<String>()

        fun flushBuffer() {
            stringComponents.add(buffer.toString())
            buffer.drop(buffer.length)
        }

        // Tokenise
        for (char in string) {
            when (char) {
                quoteCharacter -> {
                    if (isInQuote) {
                        isInQuote = false
                        flushBuffer()
                    } else {
                        isInQuote = true
                    }
                }
                whitespaceCharacter -> {
                    if (!isInQuote) {
                        flushBuffer()
                    }
                }
                else -> {
                    buffer.append(char)
                }
            }
        }

        return stringComponents
    }
}
package com.abyssaldev.commands.gateway.prefix

import net.dv8tion.jda.api.entities.Guild

class StaticPrefixStrategy(private val prefix: String) : PrefixStrategy {
    override fun getPrefix(guild: Guild?): CharSequence = prefix
}
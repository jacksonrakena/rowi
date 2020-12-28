package com.abyssaldev.abyssal_command_engine.framework.gateway.prefix

import net.dv8tion.jda.api.entities.Guild

interface PrefixStrategy {
    fun getPrefix(guild: Guild?): CharSequence
}
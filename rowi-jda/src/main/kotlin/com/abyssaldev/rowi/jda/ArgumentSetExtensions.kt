package com.abyssaldev.rowi.jda

import com.abyssaldev.rowi.core.ArgumentSet
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

val ArgumentSet.ArgumentValue.member: Member?
    get() {
        if (this.request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("ArgumentValue.member")
        return (this.request as JdaCommandRequest).guild?.getMemberById(value)
    }

val ArgumentSet.ArgumentValue.user: User?
    get() {
        if (this.request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("ArgumentValue.user")
        return (request as JdaCommandRequest).jda.getUserById(value)
    }

val ArgumentSet.ArgumentValue.channel: TextChannel?
    get() {
        if (this.request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("ArgumentValue.channel")
        return (request as JdaCommandRequest).jda.getTextChannelById(value)
    }

val ArgumentSet.ArgumentValue.role: Role?
    get() {
        if (this.request !is JdaCommandRequest) throw JdaCompatibleErrors.usedOnInvalidObject("ArgumentValue.role")
        return (request as JdaCommandRequest).guild?.getRoleById(value)
    }
package com.abyssaldev.rowi.jda

import com.abyssaldev.rowi.core.CommandEngine
import com.abyssaldev.rowi.core.RowiIntegration
import com.abyssaldev.rowi.jda.impl.NotBotContract
import com.abyssaldev.rowi.jda.impl.NotCallerContract
import com.abyssaldev.rowi.jda.types.MemberTypeParser

class RowiJdaIntegration: RowiIntegration() {
    override fun onInstall(builder: CommandEngine.Builder) {
        builder.addTypeParser(MemberTypeParser())
        builder.addArgumentContracts(hashMapOf(
            NotBotContract.id to NotBotContract(),
            NotCallerContract.id to NotCallerContract()
        ))
    }
}
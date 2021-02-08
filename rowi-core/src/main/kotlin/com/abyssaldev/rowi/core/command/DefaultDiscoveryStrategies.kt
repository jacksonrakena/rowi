package com.abyssaldev.rowi.core.command

import com.abyssaldev.rowi.core.CommandModule
import com.abyssaldev.rowi.core.CommandRequest
import com.abyssaldev.rowi.core.contracts.ArgumentContract
import com.abyssaldev.rowi.core.reflect.Command
import com.abyssaldev.rowi.core.reflect.Description
import com.abyssaldev.rowi.core.reflect.Name
import com.abyssaldev.rowi.core.util.getAnnotation
import com.abyssaldev.rowi.core.util.getAnnotations
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

typealias CommandDiscoveryStrategy = CommandModule.() -> List<CommandInstance>

class DefaultDiscoveryStrategies {
    companion object {
        val commandAnnotationDiscovery: CommandDiscoveryStrategy = {
            this::class.memberFunctions.map { member ->
                val annot = member.annotations.getAnnotation<Command>() ?: return@map null
                val parameters = member.parameters.filter { param ->
                    param.kind == KParameter.Kind.VALUE && param.name != null && !param.type.isSubtypeOf(CommandRequest::class.createType())
                }.map { param ->
                    CommandParameter(
                        name = param.annotations.getAnnotation<Name>()?.name ?: param.name!!,
                        description = param.annotations.getAnnotation<Description>()?.description ?: "",
                        type = param.type.jvmErasure,
                        contractIds = param.annotations.getAnnotations<ArgumentContract>().map { c -> c.contractId }
                    )
                }
                return@map CommandInstance(
                    name = annot.name,
                    description = annot.description,
                    invoke = member,
                    parentModule = this,
                    parameters = parameters,
                    contractIds = emptyList(),
                    moduleInheritedContractIds = emptyList()
                )
            }.filterNotNull()
        }
    }
}
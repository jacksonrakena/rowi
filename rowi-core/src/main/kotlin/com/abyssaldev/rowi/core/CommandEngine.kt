package com.abyssaldev.rowi.core

import com.abyssaldev.rowi.core.contracts.ArgumentContract
import com.abyssaldev.rowi.core.contracts.ArgumentContractable
import com.abyssaldev.rowi.core.command.CommandInstance
import com.abyssaldev.rowi.core.command.CommandParameter
import com.abyssaldev.rowi.core.contracts.SuppliedArgument
import com.abyssaldev.rowi.core.parsing.impl.DefaultContentParser
import com.abyssaldev.rowi.core.reflect.Command
import com.abyssaldev.rowi.core.reflect.Description
import com.abyssaldev.rowi.core.reflect.Name
import com.abyssaldev.rowi.core.reflect.Remainder
import com.abyssaldev.rowi.core.results.*
import com.abyssaldev.rowi.core.types.TypeParser
import com.abyssaldev.rowi.core.types.impl.BoolTypeParser
import com.abyssaldev.rowi.core.types.impl.IntTypeParser
import com.abyssaldev.rowi.core.types.impl.LongTypeParser
import com.abyssaldev.rowi.core.util.Loggable
import com.abyssaldev.rowi.core.util.getAnnotation
import com.abyssaldev.rowi.core.util.getAnnotations
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

/**
 * A Kotlin framework for building platform-independent command responders.
 */
class CommandEngine private constructor(
    /**
     * The [CommandModule]s in use by this engine.
     */
    val modules: List<CommandModule>,

    /**
     * The type parsers of this [CommandEngine].
     */
    val typeParsers: List<TypeParser<*>>,

    /**
     * The available [ArgumentContractable] instances.
     */
    val argumentContracts: HashMap<String, ArgumentContractable<*>>,

    /**
     * The available discovery strategies.
     */
    val commandDiscoveryStrategies: List<CommandModule.() -> List<CommandInstance>>
): Loggable {
    private val commands: List<CommandInstance> =
        modules.map { module -> this.commandDiscoveryStrategies.map { it(module) }.flatten() }.flatten()

    /**
     * Executes a command based on an input [String] and surrounding [CommandRequest] derivative
     * and returns the [Result]. This method assumes that the prefix has been removed from [content].
     */
    suspend fun executeSuspending(content: String, request: CommandRequest): Result {
        val argsRaw = DefaultContentParser().parse(request)
        val commandToken = argsRaw[0]
        val command = commands.firstOrNull { it.isMatch(commandToken) } ?: return CommandNotFoundResult(commandToken)
        var args = argsRaw.drop(1)

        // Invocation flags
        val flags = args.filter {it.startsWith("--") }.map { it.replace("--", "") }
        request.flags.addAll(flags)
        if (flags.isNotEmpty()) {
            args = args.filter { !it.startsWith("--") } // a hack, but sue me
        }

        // TODO command contracts

        // Parameters
        if ((command.parameters.size) > args.size) {
            return NotEnoughParametersResult(args.size, command.parameters.size, command)
        }

        val parsedArgs = mutableListOf<Any>()

        parameterParseLoop@ for ((i, parameter) in command.parameters.withIndex()) {
            if (parameter.type == String::class) {
                if (parameter.type.annotations.getAnnotation<Remainder>() != null) {
                    val remainderArg = args.subList(i, args.size).joinToString(" ")
                    parsedArgs.add(remainderArg)
                    break@parameterParseLoop
                }
                parsedArgs.add(args[i])
                continue
            }
            if (parsedArgs.size == command.parameters.size) continue
            val typeParser = typeParsers.firstOrNull {
                it::class.supertypes[0].arguments[0].type!!.isSubtypeOf(parameter.type.createType())
            } ?: return ParameterTypeParserMissingResult(parameter.type)
            val parameterValue = args[i]
            try {
                val parsedValueResult = typeParser.parse(parameterValue, request, parameter)
                if (!parsedValueResult.isSuccess) return parsedValueResult
                val parsedValue = parsedValueResult.result!!

                for (contractId in parameter.contractIds) {
                    val contract = this.argumentContracts[contractId] ?: return ArgumentContractMissingResult(contractId)
                    val contractResult = contract::class.memberFunctions.first { it.name == "evaluateContract" }.call(contract, SuppliedArgument(parameter.name, parsedValue), request) as ArgumentContract.Result<*>
                    if (!contractResult.isSuccess) {
                        return contractResult
                    }
                }
                parsedArgs.add(parsedValue)

            } catch (e: Throwable) {
                return ParameterTypeParserExceptionResult(e, parameter.type)
            }
        }

        // Finalization
        return try {
            val response = command.invoke(request, parsedArgs)
            response?.completeResponse(request)
            SuccessfulResult(response)
        } catch (e: Throwable) {
            CommandExceptionResult(e, command)
        }
    }

    /**
     * A builder pattern for [CommandEngine] instances.
     */
    class Builder {
        private var modules: MutableList<CommandModule> = mutableListOf()
        private var commandDiscoveryStrategies: MutableList<CommandModule.() -> List<CommandInstance>> = mutableListOf(
            {
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
                        parameters = parameters
                    )
                }.filterNotNull()
            }
        )
        private var typeParsers: MutableList<TypeParser<*>> = mutableListOf(
            IntTypeParser(), BoolTypeParser(), LongTypeParser()
        )
        private var argumentContracts: HashMap<String, ArgumentContractable<*>> = hashMapOf()

        /**
         * Adds the supplied [ArgumentContractable] contracts.
         */
        fun addArgumentContracts(argumentContracts: HashMap<String, ArgumentContractable<*>>): Builder {
            this.argumentContracts.putAll(argumentContracts)
            return this
        }

        /**
         * Installs the provided [RowiIntegration].
         */
        inline fun <reified Integration: RowiIntegration> install() {
            Integration::class.createInstance().onInstall(this)
        }

        /**
         * Registers a type parser for type [T].
         */
        fun <T> addTypeParser(typeParser: TypeParser<T>): Builder {
            this.typeParsers.add(typeParser)
            return this
        }

        /**
         * Adds the supplied [CommandModule] to the resulting [CommandEngine].
         * These modules will be searched for commands.
         */
        fun addModules(vararg modulesToAdd: CommandModule): Builder {
            this.modules.addAll(modulesToAdd)
            return this
        }

        /**
         * Adds the supplied discovery strategy to the resulting [CommandEngine].
         * These strategies will be used to find commands.
         */
        fun addCommandDiscoveryStrategy(strategy: CommandModule.() -> List<CommandInstance>): Builder {
            this.commandDiscoveryStrategies.add(strategy)
            return this
        }

        /**
         * Creates a [CommandEngine] instance from this [CommandEngine.Builder].
         */
        fun build(): CommandEngine {
            return CommandEngine(
                modules = this.modules,
                typeParsers = this.typeParsers,
                argumentContracts = this.argumentContracts,
                commandDiscoveryStrategies = this.commandDiscoveryStrategies
            )
        }
    }
}